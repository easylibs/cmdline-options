/*
 * 
 */
package org.easylibs.options;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.easylibs.getopt.PosixGetopt;

/**
 * <p>
 * Main command line argument parser interface. Args parses command line
 * according to the rules defined by {@link Options} which must be supplied to
 * this parser.
 * </p>
 * 
 * <p>
 * The {@link Args} and {@link Args} interfaces provides several static factory
 * methods for creating a command line argument parser. There are two types
 * which create a parser and either throw any parsing errors, see {@link #of} or
 * send the errors to user defined or default error handler which consumes the
 * errors, see {@link #ofOptional}. In the second case, the parser is returned
 * as a java Optional which the error is handled else where.
 * </p>
 * 
 * <p>
 * The Options library design separates the option definition from command line
 * parsing into two distinct and separate steps. The {@link Options.Builder} is
 * first used to define the option set to be used with the parser. The
 * {@code Args} parser then uses the options to parse the command line and store
 * the command line arguments in the {@link Options} given.
 * </p>
 * 
 * <h2>Example 1</h2>
 * <p>
 * The example defines two options a {@code -a} and a
 * {@code --file-names[=filename[,...]]}. The first option is a simple
 * {@code boolean} option and the other a list of files names. An initial list
 * object container will be created to hold the list of file names.
 * </p>
 * 
 * <code>
 * <pre>
 * final Options options = new Options.Builder()
 * 	.named("a")
 * 	.named("file-names", List.class)
 * 	.build();
 * 
 * try {
 * 	final Args args = Args.of(argv, options);
 * 	args.forEach(System.out::println);
 * } catch (ArgException e) {
 * 	e.printStackTrace();
 * }
 * </pre>
 * </code>
 * 
 * <h2>Example 2</h2>
 * <p>
 * The 2nd example also defines two options but does not throw any exception.
 * </p>
 * 
 * <code>
 * <pre>
 * final Options options = new Options.Builder()
 * 	.named("a")
 * 	.named("file-names", List.class)
 * 	.build();
 * 
 * final Optional&lt;Args&gt; optionalArgs = Args.ofOptional(argv, options);
 * optionalArgs.ifPresent(args -> args.forEach(System.out::println));
 * </pre>
 * </code>
 * 
 * <p>
 * Implementation Notes: initial release comes with a single implementation of
 * the POSIX/gnu command line argument standard defined in corresponding unix
 * manuals. In the future other parser implementations maybe provided.
 * </p>
 * 
 * @see Args
 * @see Options
 * @see Options.Builder
 * @see TypeRegistry
 */
public abstract class Args {

	private static Consumer<ArgException> defaultErrorHandler = Args::printError;

	public static Args of(String[] args, Class<?> beanContainer)
			throws UnrecognizedArgException, InvalidArgException {
		return of(args, Options.fromBeans(beanContainer));
	}

	public static Args of(String[] args, Object beanContainer)
			throws UnrecognizedArgException, InvalidArgException {
		return of(args, Options.fromBeans(beanContainer));
	}

	public static Args of(String[] args, Option<?>... options)
			throws UnrecognizedArgException, InvalidArgException {
		return new PosixArgs(args, Options.of(options))
				.parse();
	}

	/**
	 * <p>
	 * Create new instance of the default command line argument parser. The defaul
	 * is a POSIX parser see {@link PosixGetopt}. The parser will use the provided
	 * options.
	 * </p>
	 *
	 * @param args    the command line args to parse
	 * @param options the options to use for parsing and state storing
	 * @throws UnrecognizedArgException (checked) thrown by the parser if the
	 *                                  command line args contain an option looking
	 *                                  arg but no option has been defined
	 * @throws InvalidArgException      (checked) thrown by the parser if unable to
	 *                                  process an options argument. The exception
	 *                                  means that option was matched and found, but
	 *                                  its argument can not be processed or is
	 *                                  missing
	 */
	public static Args of(String[] args, Options options)
			throws UnrecognizedArgException, InvalidArgException {
		return new PosixArgs(args, options)
				.parse();
	}

	public static Args of(String[] args, Properties properties)
			throws UnrecognizedArgException, InvalidArgException {
		return of(args, Options.fromProperties(properties));
	}

	public static Optional<Args> ofOptional(String[] args, Class<?> beanContainer) {
		return ofOptional(args, Options.fromBeans(beanContainer));
	}

	public static Optional<Args> ofOptional(String[] args, Object beanContainer) {
		return ofOptional(args, Options.fromBeans(beanContainer));
	}

	public static Optional<Args> ofOptional(String[] args, Option<?>... options) {
		return ofOptional(args, Options.of(options));
	}

	/**
	 * <p>
	 * Create new instance of the default command line argument parser and
	 * dispatches instead of throws parser argument exceptions. The defaul is a
	 * POSIX parser see {@link PosixGetopt}. The parser will use the provided
	 * options.
	 * </p>
	 * 
	 * <p>
	 * Any parser errors, the checked exceptions, are dispatched using the global
	 * default error handler, {@link Args#setDefaultErrorHandler} instead
	 * of being thrown. If any error did occur, an empty optional is returned
	 * instead of the arg parser. The error handler has access to the arg parser
	 * which dispatched the error using {@link ArgException#getArgs} method.
	 * </p>
	 *
	 * @param args    the command line args to parse
	 * @param options the options to use for parsing and state storing
	 * @return the parser is returned if no error was encountered, otherwise any
	 *         empty {@link Optional#empty} is returned and the error handler is
	 *         notified
	 * @throws DuplicateOptionException    (unchecked) thrown if duplicate option is
	 *                                     passed within the options array, options
	 *                                     are equal if their option names are equal
	 *                                     {@link Option#getName()}
	 * @throws TypeMapperNotFoundException (unchecked) every option provided must
	 *                                     have a string to value type mapper
	 *                                     registered with the
	 *                                     {@link TypeRegistry}
	 *
	 */
	public static Optional<Args> ofOptional(String[] args, Options options) {
		return new PosixArgs(args, options)
				.parseOptional(defaultErrorHandler);
	}

	public static Optional<Args> ofOptional(String[] args, Properties properties) {
		return ofOptional(args, Options.fromProperties(properties));
	}

	/**
	 * Prints the error for a parser exception. This is also the default error
	 * handler see {@link Args#setDefaultErrorHandler}.
	 *
	 * @param e exception to for format and print to System.err;
	 */
	public static void printError(ArgException e) {
		System.err.printf("Error: %s%n", e.getMessage());
		System.err.flush();
	}

	public static void setDefaultErrorHandler(Consumer<ArgException> errorHandler) {
		Args.defaultErrorHandler = errorHandler;
	}

	/**
	 * All matched options to be consumed by the provided action.
	 * 
	 * <h2>Example</h2>
	 * 
	 * <code>
	 * <pre>
	 * args.forEach(option -> System.out.println(option.toString());
	 * </pre>
	 * </code>
	 * 
	 * Or using method references to simplify:
	 * 
	 * <code>
	 * <pre>
	 * args.forEach(System.out::println);
	 * </pre>
	 * </code>
	 *
	 * @param action the action which will consume all matched options
	 */
	public void forEach(Consumer<Option<?>> action) {
		getOptions().stream()
				.filter(Option::isMatched)
				.forEach(action);
	}

	/**
	 * Gets the original argument array. Note, that some parser may rearrange the
	 * array, such as when unmatched arguments are moved to the end of the array as
	 * is also the behavior of the unix <em>getopt</em> call.
	 *
	 * @return the args
	 */
	public abstract String[] getArgs();

	/**
	 * Gets the list of all matched arguments on the command line
	 *
	 * @return the matched args
	 */
	public abstract List<String> getMatchedArgs();

	/**
	 * Gets all of the matched options. Only options which were found on the command
	 * line, see {@link Option#isMatched()}, by the parser are returned.
	 *
	 * @return the matched options
	 */
	public List<Option<?>> getMatchedOptions() {
		return stream()
				.filter(Option::isMatched)
				.collect(Collectors.toList());
	}

	/**
	 * Gets the options manager currently registered with this parser.
	 *
	 * @return the options
	 */
	public abstract Options getOptions();

	/**
	 * Gets all the unmatched args. Any command line argument that was not an option
	 * and also was not matched as an argument to an option, will be returned here.
	 * Posix parser stops parsing after a double dash '--' command line argument is
	 * encountered, so any arguments after the double dash, including itself, are
	 * returned as unmatched.
	 *
	 * @return the unmatched args
	 */
	public abstract List<String> getUnmatchedArgs();

	/**
	 * Gets all of the unmatched options. All remaining options which were NOT found
	 * on the command line, see {@link Option#isUnmatched()}, by the parser are
	 * returned.
	 *
	 * @return the unmatched options
	 */
	public List<Option<?>> getUnmatchedOptions() {
		return stream()
				.filter(Option::isUnmatched)
				.collect(Collectors.toList());
	}

	/**
	 * A stream of all the options currently defined by the registered options
	 * manager.
	 *
	 * @return the stream<? extends option<?>>
	 */
	public Stream<? extends Option<?>> stream() {
		return getOptions().stream();
	}

}
