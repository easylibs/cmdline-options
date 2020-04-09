/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.easylibs.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
final class OptionsImpl implements Options {

	/** The registry. */
	protected final TypeRegistry registry;

	/** The user options. */
	protected final List<IsMutableOption<?>> userOptions = new ArrayList<>();

	/**
	 * Instantiates a new options manager.
	 *
	 * @param registry the registry
	 */
	OptionsImpl(TypeRegistry registry) {
		this.registry = registry;
	}

	/**
	 * Adds the.
	 *
	 * @param option the option
	 * @return the option
	 * @throws DuplicateOptionException    the duplicate option exception
	 * @throws TypeMapperNotFoundException the type mapper not found exception
	 */
	Option<?> add(Option<?> option)
			throws DuplicateOptionException, TypeMapperNotFoundException {

		if (!(option instanceof IsMutableOption)) {
			throw new IllegalArgumentException("immutable option " + option.getName());
		}

		if (this.userOptions.contains(option)) {
			throw new DuplicateOptionException(option.toString());
		}

		this.userOptions.add((IsMutableOption<?>) option);

		resolveTypeMapper((IsMutableOption<?>) option);

		return option;
	}

	/**
	 * Adds the all.
	 *
	 * @param options the options
	 * @return the list
	 * @throws DuplicateOptionException    the duplicate option exception
	 * @throws TypeMapperNotFoundException the type mapper not found exception
	 */
	List<Option<?>> addAll(List<Option<?>> options)
			throws DuplicateOptionException, TypeMapperNotFoundException {

		/* Check all options and make sure they are mutable */
		final Optional<Option<?>> immutable = options.stream()
				.filter(o -> !(o instanceof IsMutableOption))
				.findAny();
		if (immutable.isPresent()) {
			throw new IllegalStateException("trying to add invalid/immutable option " + immutable.get().getName());
		}

		final Optional<Option<?>> dup = options.stream()
				.filter(this.userOptions::contains)
				.findAny();
		if (dup.isPresent()) {
			throw new DuplicateOptionException(dup.get().toString());
		}

		options.forEach(this::add);

		return options;
	}

	/**
	 * Find.
	 *
	 * @param name the name
	 * @return the optional
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Optional<Option<?>> find(String name) {
		return (Optional) userOptions.stream()
				.filter(o -> o.getName().equals(name))
				.findAny();
	}

	/**
	 * Find.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the optional
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> Optional<Option<T>> find(String name, Class<T> type) {
		return userOptions.stream()
				.filter(o -> o.getName().equals(name))
				.filter(o -> type == null || (o.getType() == type))
				.map(o -> (Option<T>) o)
				.findAny();
	}

	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @return the option
	 * @throws OptionNotFoundException the option not found exception
	 */
	public Option<?> get(String name) throws OptionNotFoundException {
		final Optional<IsMutableOption<?>> option = userOptions.stream()
				.filter(o -> o.getName().equals(name))
				.findAny();

		if (!option.isPresent()) {
			throw new OptionNotFoundException(name);
		}

		return option.get();
	}

	/**
	 * Gets the.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the option
	 * @throws OptionNotFoundException the option not found exception
	 */
	@SuppressWarnings("unchecked")
	public <T> Option<T> get(String name, Class<T> type) throws OptionNotFoundException {
		Optional<IsMutableOption<?>> option = userOptions.stream()
				.filter(o -> o.getName().equals(name))
				.filter(o -> o.getType() == type)
				.findAny();

		if (!option.isPresent()) {
			throw new OptionNotFoundException(name);
		}

		return (Option<T>) option.get();
	}

	/**
	 * Gets the all.
	 *
	 * @param predicate the predicate
	 * @return the all
	 */
	@Override
	public List<Option<?>> getAll(Predicate<Option<?>> predicate) {
		return userOptions.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}

	/**
	 * Gets the all matched options. Options are only matched using {@link Args}
	 * parser. Before parsing, no options will have the {@link Option#isMatched()}
	 * return {@code true}.
	 *
	 * @return the all matched
	 */
	public List<Option<?>> getAllMatched() {
		return getAll(Option::isMatched);
	}

	/**
	 * Gets the all named.
	 *
	 * @param predicate the predicate
	 * @param names     the names
	 * @return the all named
	 * @throws OptionNotFoundException the option not found exception
	 */
	@Override
	public List<Option<?>> getAllNamed(Predicate<Option<?>> predicate, String... names) throws OptionNotFoundException {
		if (names.length == 0) {
			return getAll(predicate);
		}

		return Stream.of(names)
				.map(this::get)
				.filter(predicate)
				.collect(Collectors.toList());
	}

	/**
	 * Gets the all named.
	 *
	 * @param names the names
	 * @return the all named
	 * @throws OptionNotFoundException the option not found exception
	 */
	@Override
	public List<Option<?>> getAllNamed(String... names) throws OptionNotFoundException {
		if (names.length == 0) {
			return Collections.emptyList();
		}

		return Stream.of(names)
				.map(this::get)
				.collect(Collectors.toList());
	}

	/**
	 * Gets the all unmatched.
	 *
	 * @return the all unmatched
	 */
	public List<Option<?>> getAllUnmatched() {
		return getAll(Option::isUnmatched);
	}

	/**
	 * If matched.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the optional
	 * @throws OptionNotFoundException the option not found exception
	 */
	public <T> Optional<Option<T>> findMatched(String name, Class<T> type) throws OptionNotFoundException {

		final Option<T> opt = get(name, type);
		return Optional.ofNullable(opt.isMatched() ? opt : null);
	}

	/**
	 * Mutable stream.
	 *
	 * @return the stream
	 */
	public Stream<IsMutableOption<?>> mutableStream() {
		return userOptions.stream();
	}

	/**
	 * On match.
	 *
	 * @param <T>     the generic type
	 * @param option  the option
	 * @param type    the type
	 * @param handler the handler
	 * @return the options
	 * @throws OptionNotFoundException the option not found exception
	 */
	public <T> OptionsImpl onMatch(String option, Class<T> type, Consumer<T> handler) throws OptionNotFoundException {
		get(option, type).onMatch(handler);

		return this;
	}

	/**
	 * Resolve type mapper.
	 *
	 * @param option the option
	 * @throws TypeMapperNotFoundException the type mapper not found exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void resolveTypeMapper(IsMutableOption<?> option) throws TypeMapperNotFoundException {
		final TypeMapper mapper = (TypeMapper) registry
				.getOrElseAutoCreate(option.getType(), option.getGenericType());

		if (mapper == null) {
			throw new TypeMapperNotFoundException(
					"for option '" + option.getName() + "::" + option.getType().getSimpleName() + "'");
		}

		option.setMapper(mapper);
		option.setAccumulator(mapper.accumulator());
	}

	/**
	 * A stream of all defined options.
	 *
	 * @return the stream<? extends option<?>>
	 */
	@Override
	public Stream<? extends Option<?>> stream() {
		return userOptions.stream();
	}

}
