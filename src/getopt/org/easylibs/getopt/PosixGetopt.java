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
package org.easylibs.getopt;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

// TODO: Auto-generated Javadoc
/**
 * Close implementation of GNU/Unix getopt() method call. Both getopt() and
 * getop_long() functions are implemented through the appropriate use of the
 * correct constructor. Unlike unix call implementation, all options are parsed
 * throug a single call to {@link #getopt} method.
 * 
 * @see <a href="https://linux.die.net/man/3/getopt">getopt_long(3)</a>
 */
public final class PosixGetopt {

	/**
	 * The Class Arguments.
	 */
	private class Arguments {

		/** The args. */
		private final String[] args;

		/** The processed. */
		private int processed = 0;

		/** The code. */
		private char code;

		/**
		 * Instantiates a new arguments.
		 *
		 * @param args the args
		 */
		public Arguments(String[] args) {
			super();
			this.args = args;

		}

		/**
		 * Advance double dash.
		 */
		public void advanceDoubleDash() {
			optind++;
			nextchar = 0;
			processed = args.length;
		}

		/**
		 * Advance long no argument.
		 */
		public void advanceLongNoArgument() {
			processed++;
			optind++;
			nextchar = 0;
		}

		/**
		 * Advance long on argument.
		 */
		public void advanceLongOnArgument() {

			if (hasLongEmbededArgument()) {

				processed++;
				optind++;
				nextchar = 0;

			} else {
				processed += 2;
				optind += 2;
				nextchar = 0;
			}

		}

		/**
		 * Advance no argument.
		 */
		public void advanceShortNoArgument() {
			nextchar++;

			if (nextchar >= next().length()) {
				processed++;
				optind++;
				nextchar = 0;
			}

		}

		/**
		 * Advance on argument.
		 */
		public void advanceShortOnArgument() {
			if (hasShortEmbededArgument()) {

				processed++;
				optind++;
				nextchar = 0;

			} else {
				processed += 2;
				optind += 2;
				nextchar = 0;
			}
		}

		/**
		 * Gets the argument.
		 *
		 * @return the argument
		 */
		public String getArgument() {
			return hasShortEmbededArgument() ? getShortEmbededArgument() : getSeparateArgument();
		}

		/**
		 * Gets the embeded argument.
		 *
		 * @return the embeded argument
		 */
		public String getShortEmbededArgument() {
			return next().substring(2);
		}

		/**
		 * Gets the long argument.
		 *
		 * @return the long argument
		 */
		public String getLongArgument() {
			return hasLongEmbededArgument() ? getLongEmbededArgument() : getSeparateArgument();
		}

		/**
		 * Gets the long embeded argument.
		 *
		 * @return the long embeded argument
		 */
		public String getLongEmbededArgument() {
			String[] c = next().substring(2).split("=", 2);
			if (c.length != 2) {
				throw new IllegalStateException();
			}

			return c[1];
		}

		/**
		 * Gets the long option.
		 *
		 * @return the long option
		 */
		public String getLongOption() {
			String[] c = next().substring(2).split("=");

			return c[0];
		}

		/**
		 * Gets the separate argument.
		 *
		 * @return the separate argument
		 */
		public String getSeparateArgument() {
			return args[optind + 1];
		}

		/**
		 * Gets the short option char.
		 *
		 * @return the short option char
		 */
		public char getShortOptionChar() {
			if (nextchar == 0) {
				nextchar = 1;
			}

			return next().charAt(nextchar);
		}

		/**
		 * Checks for embeded argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasShortEmbededArgument() {
			return (nextchar == 1) && (next().length() > 2);
		}

		/**
		 * Checks for long argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasLongArgument() {
			return hasLongEmbededArgument() || hasSeparateLongArgument();
		}

		/**
		 * Checks for long embeded argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasLongEmbededArgument() {
			return next().indexOf('=') != -1;
		}

		/**
		 * Checks for next option.
		 *
		 * @param isShortOption the is short option
		 * @param isLongoption  the is longoption
		 * @return true, if successful
		 */
		public boolean hasNextOption(boolean isShortOption, boolean isLongoption) {

			while ((optind >= 0) && (optind < args.length) && (processed < args.length)) {

				if (isLongoption && isLongOption()) {
					return true;

				} else if (isShortOption && isShortOption()) {
					return true;
				}

				if (shortOptions.isPosixlyCorrect()) {

					shortOptions.error("Error: invalid non-option encountered%n");

					processed = args.length;
					break;
				}

				moveNonOptionToEnd();
			}

			code = NO_MORE_OPTIONS;
			return false;
		}

		/**
		 * Checks for separate argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasSeparateArgument() {
			return ((optind + 1) >= 0)
					&& (optind < args.length)
					&& isShortOptionInCorrectPosition()
					&& !args[optind + 1].startsWith("-");
		}

		/**
		 * Checks for separate long argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasSeparateLongArgument() {
			return ((optind + 1) >= 0)
					&& ((optind + 1) < args.length)
					&& !args[optind + 1].startsWith("-");
		}

		/**
		 * Checks for argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasShortArgument() {
			return hasShortEmbededArgument() || hasSeparateArgument();
		}

		/**
		 * Checks if is empty double dash.
		 *
		 * @return true, if is empty double dash
		 */
		public boolean isEmptyDoubleDash() {
			return "--".equals(next());
		}

		/**
		 * Checks if is long option.
		 *
		 * @return true, if is long option
		 */
		public boolean isLongOption() {
			return next().startsWith("--") && next().length() > 2;
		}

		/**
		 * Checks if is option last char.
		 *
		 * @return true, if is option last char
		 */
		public boolean isShortOptionInCorrectPosition() {
			return (optind >= 0)
					&& (optind < args.length)
					&& ((nextchar == 1) || (nextchar == next().length() - 1));
		}

		/**
		 * Checks if is short option.
		 *
		 * @return true, if is short option
		 */
		public boolean isShortOption() {
			return next().charAt(0) == '-' && next().length() > 1 && next().charAt(1) != '-';
		}

		/**
		 * Move non option to end.
		 */
		private void moveNonOptionToEnd() {
			final boolean isLastArg = (optind == args.length - 1);

			if (!isLastArg) {
				String arg = args[optind];
				for (int i = optind; i < (args.length - 1); i++) {
					args[i] = args[i + 1];
				}

				args[args.length - 1] = arg;
			}

			processed++;
			nextchar = 0;
		}

		/**
		 * Next.
		 *
		 * @return the string
		 */
		public String next() {
			return args[optind];
		}

	}

	/**
	 * The Class LongOptions.
	 */
	private class LongOptions {

		/** The options. */
		private final List<PosixLongOption> options;

		/**
		 * Instantiates a new long options.
		 *
		 * @param options the options
		 */
		private LongOptions(List<PosixLongOption> options) {
			this.options = options;
		}

		/**
		 * Instantiates a new long options.
		 *
		 * @param options the options
		 */
		private LongOptions(PosixLongOption[] options) {
			this(Arrays.asList(options));
		}

		/**
		 * Error.
		 *
		 * @param fmt  the fmt
		 * @param args the args
		 */
		private void error(String fmt, Object... args) {
			if (isPrintError()) {
				System.err.printf(fmt, args);
			}

		}

		/**
		 * Gets the option.
		 *
		 * @param optionString the option string
		 * @return the option
		 */
		public PosixLongOption getOption(String optionString) {
			return options.stream()
					.filter(o -> o.getName().equals(optionString))
					.findAny()
					.orElseThrow(IllegalStateException::new);
		}

		/**
		 * Checks for option.
		 *
		 * @param optionString the option string
		 * @return true, if successful
		 */
		public boolean hasOption(String optionString) {
			return options.stream()
					.filter(o -> o.getName().equals(optionString))
					.findFirst()
					.isPresent();
		}

		/**
		 * Checks if is prints the error.
		 *
		 * @return true, if is prints the error
		 */
		public boolean isPrintError() {
			return shortOptions.isPrintError();
		}

	}

	/**
	 * The Class ShortOptions.
	 */
	private class ShortOptions {

		/** The definition. */
		private final String definition;

		/** The index. */
		private int index;

		/**
		 * Instantiates a new short options.
		 *
		 * @param definition the definition
		 */
		public ShortOptions(String definition) {
			this.definition = definition;
		}

		/**
		 * Error.
		 *
		 * @param fmt  the fmt
		 * @param args the args
		 */
		private void error(String fmt, Object... args) {
			if (isPrintError()) {
				System.err.printf(fmt, args);
			}

		}

		/**
		 * Checks for argument.
		 *
		 * @return true, if successful
		 */
		public boolean hasArgument() {
			return ((index + 1) < definition.length()) && (definition.charAt(index + 1) == ':');
		}

		/**
		 * Checks for option.
		 *
		 * @param ch the ch
		 * @return true, if successful
		 */
		public boolean hasOption(char ch) {
			this.index = definition.indexOf(ch);

			return (this.index != -1) || (ch == '-');
		}

		/**
		 * Checks if is argument optional.
		 *
		 * @return true, if is argument optional
		 */
		public boolean isArgumentOptional() {
			return ((index + 2) < definition.length())
					&& (definition.charAt(index + 1) == ':')
					&& (definition.charAt(index + 2) == ':');
		}

		/**
		 * Checks if is posixly correct.
		 *
		 * @return true, if is posixly correct
		 */
		public boolean isPosixlyCorrect() {
			return posixlyCorrect || !definition.isEmpty() && (definition.charAt(0) == '+');
		}

		/**
		 * Checks if is prints the error.
		 *
		 * @return true, if is prints the error
		 */
		public boolean isPrintError() {
			return (opterr != 0) || !definition.isEmpty() && (definition.charAt(0) != ':');
		}

	}

	/**
	 * A status code that indicates there are no more options on the command line.
	 */
	public final static char NO_MORE_OPTIONS = 255;

	/**
	 * An error code that indicates the command line option encountered is not
	 * recognized.
	 */
	public final static char ERROR_UNRECOGNIZED_OPT = '?';

	/**
	 * An error code that indicates that a recognized option is missing a required
	 * argument on the command line.
	 */
	public final static char ERROR_MISSING_ARG = ':';

	/** A status code that indicates the option encountered is of the long form. */
	public final static char LONG_OPT = 0;

	/** The optarg. */
	private String optarg;

	/** The optind. */
	private int optind = 0;

	/** The opterr. */
	private char opterr = 0;

	/** The nextchar. */
	private int nextchar = 0;

	/** The opt. */
	private char opt;

	/** The posixly correct. */
	private boolean posixlyCorrect;

	/** The args. */
	private Arguments args;

	/** The short options. */
	private final ShortOptions shortOptions;

	/** The long options. */
	private final Optional<LongOptions> longOptions;

	/** The optmatch. */
	private String optmatch;

	/**
	 * The longmatch.
	 *
	 * @param optstring the optstring
	 */
//	private final Optional<Consumer<PosixLongOption>> longmatch;

	/**
	 * Instantiates a new posix getopt, akin to unix {@code getopt()} call, using
	 * only short options.
	 *
	 * @param optstring short option string definition
	 * @see <a href="https://linux.die.net/man/3/getopt">getopt_long(3)</a>
	 */
	public PosixGetopt(String optstring) {
		this(optstring, (List<PosixLongOption>) null);
	}

	/**
	 * Instantiates a new posix getopt, akin to unix {@code getopt_long()} call,
	 * using both short and long options with a callback notification of long option
	 * selection.
	 *
	 * @param optstring short option string definition
	 * @param longopts  long option list of definitions
	 * @see <a href="https://linux.die.net/man/3/getopt">getopt_long(3)</a>
	 */
	public PosixGetopt(
			String optstring,
			List<PosixLongOption> longopts) {

		Objects.requireNonNull(optstring, "optstring");

		this.longOptions = Optional.ofNullable(longopts)
				.filter(list -> !list.isEmpty())
				.map(LongOptions::new);

		this.shortOptions = new ShortOptions(optstring);
	}

	/**
	 * Instantiates a new posix getopt, akin to unix {@code getopt_long()} call,
	 * using both short and long options.
	 *
	 * @param optstring short option string definition
	 * @param longopts  long option list of definitions
	 * @see <a href="https://linux.die.net/man/3/getopt">getopt_long(3)</a>
	 */
	public PosixGetopt(String optstring, PosixLongOption[] longopts) {
		this(optstring, Arrays.asList(longopts));
	}

	/**
	 * Clear.
	 */
	public void clear() {
		optind = 0;
		optarg = null;
	}

	/**
	 * Gets the short option character currently selected.
	 *
	 * @return the option character
	 */
	public char getopt() {
		return getopt(null);
	}

	/**
	 * Gets the opt.
	 *
	 * @param longmatch the longmatch
	 * @return the opt
	 */
	public char getopt(Consumer<PosixLongOption> longmatch) {
		Objects.requireNonNull(args, "args; see PosixGetopt.parseArgs");

		if (longOptions.isPresent()) {
			return scanLongOptions(args, longOptions.get(), Optional.ofNullable(longmatch));

		} else {
			return scanShortOptions(args, shortOptions);
		}
	}

	/**
	 * The current index into the command line argument array supplied. You should
	 * always check to make sure the index is still within argv array bound with
	 * code like this {@code index() < argv.length}.
	 *
	 * @return the int
	 */
	public int index() {
		return optind;
	}

	/**
	 * Returns the current index and increaments the index (post) counter by the
	 * specified amount. You should always check to make sure the index is still
	 * within argv array bound with code like this {@code index() < argv.length}.
	 *
	 * @param inc the inc
	 * @return the int
	 */
	public int index(int inc) {
		int old = optind;
		optind += inc;
		return old;
	}

	/**
	 * This will return short options argument or null if the argument is optional.
	 *
	 * @return the string
	 */
	public String optarg() {
		return optarg;
	}

	/**
	 * If any error occured bacause an option is missing a required argument, the
	 * method will return the option character related to the error.
	 *
	 * @return the option char
	 */
	public char optchar() {
		return opt;
	}

	/**
	 * The option matched for both short and long options as a string.
	 *
	 * @return the matched option
	 */
	public String optmatch() {
		return optmatch;
	}

	/**
	 * Sets the current command line argument array. The index into the array is
	 * also reset back to 0.
	 *
	 * @param args the args
	 */
	public void setArgs(String[] args) {
		Objects.requireNonNull(args, "args");
		this.args = new Arguments(args);
		this.optind = 0;
		reset();
	}

	/**
	 * Reset.
	 */
	private void reset() {
		this.optarg = null;
		this.opt = 0;
	}

	/**
	 * Scan long options.
	 *
	 * @param args      the args
	 * @param opts      the opts
	 * @param longmatch the longmatch
	 * @return the char
	 */
	private char scanLongOptions(Arguments args, LongOptions opts, Optional<Consumer<PosixLongOption>> longmatch) {

		reset();

		if (!args.hasNextOption(true, true)) {
			return args.code;
		}

		if (args.isShortOption()) {
			return scanShortOptions(args, shortOptions);
		}

		if (args.isEmptyDoubleDash()) {
			args.advanceDoubleDash();

			return 0;
		}

		final String optionString = args.getLongOption();
		if (!opts.hasOption(optionString)) {

			opts.error("Error: unrecognized option '%s'%n", optionString);

			return ERROR_UNRECOGNIZED_OPT;
		}
		this.optmatch = args.next();

		final PosixLongOption option = opts.getOption(optionString);

		if (option.hasArgument()) {

			if (args.hasLongArgument()) {
				this.optarg = args.getLongArgument();
				args.advanceLongOnArgument();

			} else if (!args.hasLongArgument() && option.isArgumentOptional()) {
				args.advanceLongNoArgument();

			} else {
				opts.error("Error: option '%s' missing required argument%n", optionString);

				return ERROR_MISSING_ARG;
			}
		} else {
			args.advanceLongNoArgument();
		}

		longmatch.ifPresent(h -> h.accept(option));

		return option.isShortCodePresent() ? option.getShortCode() : LONG_OPT;
	}

	/**
	 * Scan short options.
	 *
	 * @param args the args
	 * @param opts the opts
	 * @return the char
	 */
	private char scanShortOptions(Arguments args, ShortOptions opts) {

		reset();

		if (!args.hasNextOption(true, false)) {
			return args.code;
		}

		if (args.isEmptyDoubleDash()) {
			args.advanceDoubleDash();

			return 0;
		}

		final char optionChar = args.getShortOptionChar();
		if (!opts.hasOption(optionChar)) {
			opt = optionChar;
			this.optmatch = "" + opt;

			opts.error("Error: unrecognized option '%s'%n", optionChar);

			return ERROR_UNRECOGNIZED_OPT;
		}

		/* a: */
		if (opts.hasArgument()) {

			if (!args.isShortOptionInCorrectPosition()) {
				opts.error("Error: option '%s' takes an agrument and must be at the end of a group%n",
						optionChar);

				opt = optionChar;
				return ':';
			}

			if (args.hasShortArgument()) {
				optarg = args.getArgument();
				args.advanceShortOnArgument();

			} else if (!args.hasShortArgument() && opts.isArgumentOptional()) {
				args.advanceShortNoArgument();

			} else {
				opts.error("Error: option '%s' missing required argument%n",
						optionChar);

				opt = optionChar;
				return ERROR_MISSING_ARG;
			}

		} else {
			args.advanceShortNoArgument();
		}

		return optionChar;
	}

	/**
	 * Sets the posixly correct.
	 *
	 * @param b the new posixly correct
	 */
	public void setPosixlyCorrect(boolean b) {
		this.posixlyCorrect = b;
	}

	/**
	 * Sets the prints the error.
	 *
	 * @param b the new prints the error
	 */
	public void setPrintError(boolean b) {
		this.opterr = (char) (b ? 1 : 0);
	}

}
