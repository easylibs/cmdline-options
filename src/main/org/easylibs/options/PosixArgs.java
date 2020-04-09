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

import static org.easylibs.getopt.PosixGetopt.LONG_OPT;
import static org.easylibs.getopt.PosixGetopt.NO_MORE_OPTIONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.easylibs.getopt.PosixGetopt;
import org.easylibs.getopt.PosixLongOption;
import org.easylibs.getopt.PosixLongOption.HasArg;

// TODO: Auto-generated Javadoc
/**
 * The Class PosixArgs.
 */
class PosixArgs extends Args {

	/** The args. */
	private final String[] args;

	/** The posix. */
	private PosixGetopt posix;

	/** The unmatched args. */
	private final List<String> unmatchedArgs = new ArrayList<>();

	/** The matched args. */
	private final List<String> matchedArgs = new ArrayList<>();

	/** The options. */
	private final OptionsImpl options;

	/**
	 * Instantiates a new posix args.
	 *
	 * @param args    the args
	 * @param options the options
	 */
	PosixArgs(String[] args, Options options) {
		this.options = (OptionsImpl) options;
		this.args = args;
	}

	/**
	 * Builds the.
	 *
	 * @return the posix getopt
	 */
	private PosixGetopt build() {

		final String shortOptions = buildShortOptions();
		final List<PosixLongOption> longOptions = buildLongOptions();

//		System.out.printf("PosixGetopt('%s', %s)%n",
//				shortOptions,
//				longOptions.stream().map(o -> o.name()).collect(Collectors.toList()));

		return new PosixGetopt(
				shortOptions,
				longOptions);
	}

	/**
	 * Builds the long option.
	 *
	 * @param option the option
	 * @return the posix long option
	 */
	private PosixLongOption buildLongOption(IsMutableOption<?> option) {
		if (option.getName().length() < 2) {
			throw new IllegalArgumentException();
		}

		final String name = option.getName();
		final HasArg hasArg = option.max() == 0
				? HasArg.NO
				: option.isOptional()
						? HasArg.OPTIONAL
					: HasArg.REQUIRED;

		final PosixLongOption longOption = new PosixLongOption(name, hasArg);
		longOption.setOpaque(option);

		return longOption;
	}

	/**
	 * Builds the long options.
	 *
	 * @return the list
	 */
	private List<PosixLongOption> buildLongOptions() {
		return options.mutableStream()
				.filter(o -> (o.getName().length() > 1))
				.map(this::buildLongOption)
				.collect(Collectors.toList());

	}

	/**
	 * Builds the short option.
	 *
	 * @param option the option
	 * @return the string
	 */
	private String buildShortOption(IsMutableOption<?> option) {

		if (option.getName().length() != 1) {
			throw new IllegalArgumentException();
		}

		final String arg = (option.max() > 0 ? ":" : "") + (option.isOptional() ? ":" : "");

		return option.getName() + arg;
	}

	/**
	 * Builds the short options.
	 *
	 * @return the string
	 */
	private String buildShortOptions() {
		return options.mutableStream()
				.filter(o -> (o.getName().length() == 1))
				.map(this::buildShortOption)
				.reduce(":", (a, b) -> a + b);
	}

	/**
	 * Gets the args.
	 *
	 * @return the args
	 */
	@Override
	public String[] getArgs() {
		return this.args;
	}

	/**
	 * Gets the matched args.
	 *
	 * @return the matched args
	 */
	@Override
	public List<String> getMatchedArgs() {
		return matchedArgs;
	}

	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	@Override
	public Options getOptions() {
		return options;
	}

	/**
	 * Gets the unmatched args.
	 *
	 * @return the unmatched args
	 */
	@Override
	public List<String> getUnmatchedArgs() {
		return unmatchedArgs;
	}

	/**
	 * On option match.
	 *
	 * @param option           the option
	 * @param exceptionHandler the exception handler
	 */
	private void onOptionMatch(IsMutableOption<?> option, Consumer<InvalidArgException> exceptionHandler) {

		@SuppressWarnings("unchecked")
		final IsMutableOption<Object> mutable = (IsMutableOption<Object>) option;
		final TypeMapper<?> mapper = option.getMapper();

		if (option.max() > 0) {

			final String arg = posix.optarg();
			if (arg != null) {

				try {
					mutable.setValue(mapper.mapFrom(arg));
				} catch (Throwable e) {
//					e.printStackTrace();
					exceptionHandler.accept(
							new InvalidArgException(this, "option '" + posix.optmatch() + "'"
									+ " invalid argument", option, e));
					return;
				}

			} else {

				mutable.setValue(mapper.defaultValue());
			}
		} else {
			mutable.setValue(mapper.defaultValue());
		}
	}

	/**
	 * On option match.
	 *
	 * @param option           the option
	 * @param exceptionHandler the exception handler
	 */
	private void onOptionMatch(PosixLongOption option, Consumer<InvalidArgException> exceptionHandler) {
		option.getOpaque(IsMutableOption.class)
				.ifPresent(mu -> onOptionMatch(mu, exceptionHandler));
	}

	/**
	 * Parses the.
	 *
	 * @return the posix args
	 * @throws UnrecognizedArgException the unrecognized arg exception
	 * @throws InvalidArgException      the invalid arg exception
	 */
	public Args parse()
			throws UnrecognizedArgException, InvalidArgException {

		this.posix = build();
		posix.setArgs(args);
		posix.setPosixlyCorrect(false);

		final AtomicReference<InvalidArgException> eHandler = new AtomicReference<>();

		char opt = 0;
		while ((opt = posix.getopt(o -> onOptionMatch(o, eHandler::set))) != NO_MORE_OPTIONS) {

			if (eHandler.get() != null) {
				throw eHandler.get();
			}

			switch (opt) {

			case LONG_OPT: {
				continue; // Handled through onOptionMatch
			}

			case PosixGetopt.ERROR_MISSING_ARG: {
				final Option<?> option = options.get("" + posix.optchar());
				final String arg = "'" + args[posix.index()] + "'";

				throw new InvalidArgException(this, "required arg is missing for option " + arg, option);
			}

			case PosixGetopt.ERROR_UNRECOGNIZED_OPT: {
				final String arg = "'" + args[posix.index()] + "'";
				throw new UnrecognizedArgException(this, "option not recognized " + arg, posix.optarg());
			}

			default:
				final String optName = "" + opt;

				options.mutableStream()
						.filter(o -> o.getName().equals(optName))
						.forEach(o -> onOptionMatch(o, eHandler::set));

				if (eHandler.get() != null) {
					throw eHandler.get();
				}
			}
		}

		for (int i = 0; i < posix.index(); i++) {
			matchedArgs.add(args[i]);
		}

		while (posix.index() < args.length) {
			unmatchedArgs.add(args[posix.index(1)]);
		}

		validate();

		return this;
	}

	/**
	 * Parses the.
	 *
	 * @param errorHandler the error handler
	 * @return the optional
	 */
	public Optional<Args> parseOptional(Consumer<ArgException> errorHandler) {
		try {
			return Optional.of(parse());
		} catch (UnrecognizedArgException | InvalidArgException e) {

			errorHandler.accept(e);

			return Optional.empty();
		}
	}

	/**
	 * Validate.
	 *
	 * @throws UnrecognizedArgException the unrecognized arg exception
	 * @throws InvalidArgException      the invalid arg exception
	 */
	private void validate() throws UnrecognizedArgException, InvalidArgException {
		// Placeholder for validations such as min/max checks, etc...
	}

}
