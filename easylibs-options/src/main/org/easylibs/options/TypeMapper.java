/*
 * 
 */
package org.easylibs.options;

/**
 * The {@code TypeMapper} is used to map between command line string arguments
 * and a other java types. For example if an option is expecting an integer,
 * there exists a mapping from string to integer type.
 * <p>
 * The type mappings are maintained in a private type registry. You can add or
 * overrdide mappings using the {@link TypeRegistry#register} method or
 * {@link Options.Builder#mapType} method during the options creation. This
 * allows new types or different implementations for different mappings.
 * </p>
 * 
 * <p>
 * When the type registry does not have an explicit type mapper defined for a
 * type, it tries to implicitely create a type mapper using several internal
 * algorithms. Types which are arrays, collection or enums, have implicit
 * mappings which are called recursively on their component types as well. So
 * arrays or arrays or collections of arrays, etc are all possible.
 * </p>
 * 
 * <p>
 * All enum types are also implicitely mapped the enum's
 * {@link Enum#valueOf(Class, String)} is used to create an enum constant by
 * comparing all the enum constant names (case insensitive). The user can
 * override the mapper for a specific enum type, to choose a different mapping
 * algorithm such as case sensitive one or partial name match, etc.
 * </p>
 * <H2>Example - enum usage</H2>
 * 
 * <code>
 * <pre>
 * public enum MyEnum {A,B,C}
 * 
 * TypeRegistry.register(MyEnum.class, str -> MyEnum.valueOf(str));
 * Option&lt;MyEnum&gt; opion1 = Option.of("option1", MyEnum.class);
 * </pre>
 * </code>
 * <p>
 * later in code when you are working with the option, you can simple get the
 * enum value.
 * </p>
 * 
 * <code>
 * <pre>
 * MyEnum value = option1.getValue();
 *  // or
 * Optional&lt;MyEnum&gt; valueOptional = option1.getOptionalValue();
 * </pre>
 * </code>
 * 
 * @param <T> the generic type
 */
public interface TypeMapper<T> {

	/**
	 * Map from.
	 *
	 * @param value the value
	 * @return the t
	 * @throws Throwable the throwable
	 */
	T mapFrom(String value) throws Throwable;

	/**
	 * Priority.
	 *
	 * @return the int
	 */
	default int priority() {
		return 0;
	}

	default T defaultValue() {
		return null;
	}

	/**
	 * Accumulator.
	 *
	 * @return the accumulator
	 */
	default Accumulator<T> accumulator() {
		return Accumulator.identity();
	}
}
