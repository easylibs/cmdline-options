/*
 * 
 */
package org.easylibs.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Options manager. Options manager is used to manage different types of options
 * which are then used with {@link Args} parser to perform matches between the
 * options in manager's cache and command line arguments.
 * <p>
 * To build an option manager and register options with it, you must use the
 * {@link Builder} object and call the {@link Builder#build()} method to build
 * get an instance.
 * <p>
 * <p>
 * The Options class also provides some prebuilt factory static methods such as
 * {@link #of}, {@link #fromBeans} and {@link #fromProperties} for the most
 * common types of builds. However note, that these simply invoke the builder
 * and you are able to build yourself a complex set of option arrangements.
 * </p>
 * <p>
 * Alternatively some concrete implementations of {@link Arg} parsers also
 * provide factory methods to perform an option manager build and parsing in a
 * single step. Static factory methods in the form {@code of() and ofOptional}
 * found in {@link Args} are used to build options and parse arguments in a
 * single step.
 * </p>
 * 
 * <p>
 * For example {@link Args} and {@link Args} parsers will build {@code Options}
 * and parse the command line in one step.
 * </p>
 * <h2>Examples</h2>
 * 
 * <p>
 * Options built from class fields as beans
 * <p>
 * 
 * <code>
 * <pre>
 * public class MyClass {
 *   &#64;Arg
 *   static int optionA;
 * }
 * Options options = Options.fromBeans(MyClass.class);
 * </pre>
 * </code>
 * 
 * <p>
 * or options build from java properties
 * </p>
 * 
 * <code>
 * <pre>
 * Properties properties = new Properties();
 * properties.setProperty("optionA", "");
 * Options options = Options.fromProperties(MyClass.class);
 * </pre>
 * </code>
 * <p>
 * Lastly simple options which you have to query manually
 * </p>
 * 
 * <code>
 * <pre>
 * Options options = new Options.Builder()
 * 	.named("optionA", int.class)
 * 	.build();
 * 
 * Option&lt;Integer&gt; optionA = options.get("optionA", int.class);
 * 
 * Args args = Args.of(argv, options);
 * System.out.println(optionA.toString());
 * </pre>
 * </code>
 * 
 */
public interface Options extends OptionGroup {

	/**
	 * A builder for options groups.
	 */
	public static class Builder {

		/** The registry. */
		private final TypeRegistry registry;

		/** The list. */
		private final List<Option<?>> list = new ArrayList<>();

		/** The actions. */
		private final List<Consumer<Option<?>>> actions = new ArrayList<>();

		/**
		 * Instantiates a new builder.
		 */
		public Builder() {
			this.registry = TypeRegistry.global();
		}

		/**
		 * Builds the.
		 *
		 * @return the options
		 */
		public Options build() {

			final OptionsImpl options = new OptionsImpl(registry);

			list.forEach(option -> actions.forEach(action -> action.accept(option)));
			options.addAll(list);

			return options;
		}

		/**
		 * Creates the.
		 *
		 * @param name the name
		 * @return the builder
		 */
		public Builder create(String name) {
			return using(Option.of(name));
		}

		/**
		 * Creates the.
		 *
		 * @param <T>  the generic type
		 * @param name the name
		 * @param type the type
		 * @return the builder
		 */
		public <T> Builder create(String name, Class<T> type) {
			return using(Option.of(name, type));
		}

		/**
		 * Creates the.
		 *
		 * @param <T>     the generic type
		 * @param name    the name
		 * @param type    the type
		 * @param handler the handler
		 * @return the builder
		 */
		public <T> Builder create(String name, Class<T> type, Consumer<T> handler) {
			return using(Option.of(name, type).onMatch(handler));
		}

		/**
		 * A group of sub-options.
		 *
		 * @param option the option
		 * @param subs   the subs
		 * @return the builder
		 */
		public Builder group(char option, Consumer<Builder> subs) {

			final Builder suboptionBuilder = new Builder() {

				@Override
				public Builder group(char option, Consumer<Builder> subs) {
					throw new UnsupportedOperationException("suboptions of suboptions is not supported");
				}

			};

			subs.accept(suboptionBuilder);

			return this;
		}

		/**
		 * Map type.
		 *
		 * @param <T>    the generic type
		 * @param type   the type
		 * @param mapper the mapper
		 * @return the builder
		 */
		public <T> Builder mapType(Class<T> type, TypeMapper<T> mapper) {
			this.registry.set(type, mapper);

			return this;
		}

		/**
		 * On match.
		 *
		 * @param <T>     the generic type
		 * @param name    the name
		 * @param type    the type
		 * @param handler the handler
		 * @return the builder
		 * @throws OptionNotFoundException the option not found exception
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <T> Builder onMatch(final String name, final Class<T> type, final Consumer<T> handler)
				throws OptionNotFoundException {

			this.actions.add(option -> {
				if (option.getName().equals(name)) {
					option.onMatch((Consumer) handler);
				}
			});

			return this;
		}

		/**
		 * Using.
		 *
		 * @param option the option
		 * @return the builder
		 */
		public Builder using(Option<?> option) {
			list.add(option);
			return this;
		}

		/**
		 * Using all.
		 *
		 * @param options the options
		 * @return the builder
		 */
		public Builder usingAll(Collection<Option<?>> options) {
			list.addAll(options);
			return this;
		}

		/**
		 * Using all.
		 *
		 * @param options the options
		 * @return the builder
		 */
		public Builder usingAll(Option<?>[] options) {
			list.addAll(Arrays.asList(options));
			return this;
		}
	}

	/**
	 * Factory options builder that builds by scanning for beans.
	 *
	 * @param beanContainer the bean container
	 * @return the options
	 * @see BeanOption#listOptions(Class...)
	 */
	public static Options fromBeans(Class<?> beanContainer) {
		return fromBeans((Object) beanContainer);
	}

	/**
	 * Factory options builder that builds by scanning for beans.
	 *
	 * @param beanContainer the bean container
	 * @return the options
	 * @see BeanOption#listOptions(Class...)
	 */
	public static Options fromBeans(Object beanContainer) {
		return new Options.Builder()
				.usingAll(BeanOption.listOptions(beanContainer))
				.build();
	}

	/**
	 * Factory options builder that builds by scanning java properties from which
	 * options are created.
	 *
	 * @param properties the properties
	 * @return the options
	 * @see PropertyOption#listOptions(Properties)
	 */
	public static Options fromProperties(Properties properties) {
		return new Options.Builder()
				.usingAll(PropertyOption.listOptions(properties))
				.build();
	}

	/**
	 * Factory options builder. Builds options manager from the collection of
	 * options specified.
	 *
	 * @param options the options to build the options manager from
	 * @return the new options manager
	 */
	public static Options of(Collection<Option<?>> options) {
		return new Options.Builder()
				.usingAll(options)
				.build();
	}

	/**
	 * Factory options builder. Builds options manager from zero or more options
	 * specified.
	 *
	 * @param options the options to build the options manager from
	 * @return the new options manager
	 */
	public static Options of(Option<?>... options) {
		return new Options.Builder()
				.usingAll(options)
				.build();
	}

}
