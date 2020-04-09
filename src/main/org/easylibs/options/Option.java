/*
 * 
 */
package org.easylibs.options;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 * An application defined command line option. An option consists of an option
 * name and an option value. The name is used to match an option with the
 * command line arguments. Options may consume one or more arguments from the
 * command line, which are typically then returned as option values after being
 * mapped to java types.
 * </p>
 * 
 * <p>
 * Option value types can be of any type as long as there is a mapping for the
 * type. The most basic option doesn't take any additonal arguments and by
 * default its value type is mapped to be {@code boolean}. While other options
 * may take 1 or more arguments and can be mapped to privitive types, arrays,
 * collections and even enums. To register new types or override existing ones
 * use the method {@link TypeRegistry#register}.
 * </p>
 * 
 * <p>
 * Option interface has several different implementations that simplify working
 * with command line options. For example there are simple options, bean
 * options, java properties options and others. When the option value is
 * changed, the various implementations also sync the value with their
 * implementation and underlying container. For example a bean option will also
 * possibly set a class field value as the option's value is changed.
 * </p>
 * 
 * <p>
 * Options are grouped using an {@link Options} group and are then passed to
 * command line parser {@link Args} for parsing all the command line arguments.
 * For convenience, the {@link Args} command line parser contains sevaral static
 * methods that combine the steps neccessary to create an option and parse the
 * command line, but for more control, those steps should be done separately.
 * </p>
 *
 * <h2>Examples</h2>
 * 
 * <code>
 * <pre>
 * Option&lt;Boolean&gt; option1 = Option.of("option1");
 * Option&lt;Integer&gt; option2 = Option.of("option2", int.class);
 * </pre>
 * </code>
 * 
 * <p>
 * Here we are working with an enum option, where the command line argument will
 * be converted (case insensitive) to the enum constant and set as the option's
 * value. Further more we demonstrate that its easy to define arrays and
 * collections of any type, in this example's case using our {@code MyEnum}
 * generic type.
 * </p>
 * 
 * <code>
 * <pre>
 * public enum MyEnum {A,B,C}
 * Option&lt;MyEnum&gt; option3 = Option.of("option3", MyEnum.class);
 * Option&lt;MyEnum[]&gt; option4 = Option.of("option4", MyEnum[].class);
 * Option&lt;Collection&lt;MyEnum&gt;&gt; option5 = Option.of("option5", Collection.class, MyEnum.class);
 * </pre>
 * </code>
 * 
 * <p>
 * Lastly we can pass our options to a command line parser and if any options we
 * defined appear on the comamnd line, their arguments will be set as option
 * values. In our case that is {@code boolean}, {@code int}, {@code MyEnum},
 * array of {@code MyEnum} and a collection of the same type.
 * </p>
 * 
 * <code>
 * <pre>
 * String[] argv = ...; // from the command line
 * Args args = Args.of(argv, option1, option2, option3, option4, option5);
 * args.getMatchedOptions.forEach(option -> System.out.println(option));
 * </pre>
 * </code>
 * 
 * <p>
 * <b>Note:</b> The Args parser raises an exception on any parsing errors which
 * should be caught and handled.
 * </p>
 * 
 * <p>
 * <b>Note:</b> Even though we define a new enum class, the parser, or more
 * accurately the {@link TypeRegistry} creates an automatic mapping for us to
 * map the command line string argument to our enum constant(s).
 * </p>
 * 
 * <p>
 * <b>Note:</b> The default type mapper uses case insensitive comparison of the
 * command line argument to enum constant names. To override that mapping and
 * make it case sensitve for example, register a new mapper before the option is
 * used by the parser such as
 * {@code TypeRegistry.register(MyEnum.class, MyEnum::valueOf)}.
 * </p>
 * 
 * 
 * @param <T> the generic type of the option's value
 */
public interface Option<T> {

	/**
	 * Named.
	 *
	 * @param name the name
	 * @return the option
	 */
	public static Option<Boolean> of(String name) {
		return new SimpleOption<>(name, boolean.class, false);
	}

	/**
	 * Named.
	 *
	 * @param <U>     the generic type
	 * @param name    the name
	 * @param type    the type
	 * @param handler the handler
	 * @return the option
	 */
	public static <U> Option<U> of(String name, Class<U> type, Consumer<U> handler) {
		return of(name, type).onMatch(handler);
	}

	/**
	 * Named.
	 *
	 * @param <U>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the option
	 */
	public static <U> Option<U> of(String name, Class<U> type) {
		return new SimpleOption<>(name, type, false);
	}

	/**
	 * Named optional.
	 *
	 * @param <U>     the generic type
	 * @param name    the name
	 * @param type    the type
	 * @param handler the handler
	 * @return the option
	 */
	public static <U> Option<U> ofOptional(String name, Class<U> type, Consumer<U> handler) {
		return ofOptional(name, type).onMatch(handler);
	}

	/**
	 * Named optional.
	 *
	 * @param <U>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the option
	 */
	public static <U> Option<U> ofOptional(String name, Class<U> type) {
		return new SimpleOption<>(name, type, true);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String getName();

	/**
	 * Gets the current value of the option. If the option is not matched the value
	 * will be set to its default value.
	 *
	 * @return the option value and can be the default value or even null
	 */
	T getValue();

	/**
	 * Gets the value only when the option is matched and the value is there.
	 *
	 * @return the optional value, this method never returns null
	 */
	default Optional<T> getOptionalValue() {
		return Optional.ofNullable(getValue());
	}

	/**
	 * Checks if the option is matched on the command line
	 *
	 * @return true, if found on the command line, otherwise false
	 */
	boolean isMatched();

	/**
	 * The option was never matched with the command line
	 *
	 * @return true, if is unmatched
	 */
	default boolean isUnmatched() {
		return !isMatched();
	}

	/**
	 * Minimum number of arguments required by this option
	 *
	 * @return the minimum count
	 */
	int min();

	/**
	 * Maximum number of arguments required by this option
	 *
	 * @return the maximum count
	 */
	int max();

	/**
	 * When the option is matched on the command line, execute the supplied action
	 * on the option's value
	 *
	 * @param action the action to perform on a successful option match
	 * @return the current option for call chaining
	 */
	Option<T> onMatch(Consumer<T> action);
}
