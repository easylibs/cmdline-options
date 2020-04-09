package org.easylibs.options.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easylibs.options.Arg;
import org.easylibs.options.Args;
import org.easylibs.options.InvalidArgException;
import org.easylibs.options.OptionException;
import org.easylibs.options.UnrecognizedArgException;

public class Example5 {

	/** Set options as beans in this object */
	private static class CmdlineState {

		/**
		 * Enums are auto registered with {@code OptionsRegistry} and constants are
		 * selected using case insensitive comparison with command line args
		 */
		public enum Priority {
			LOW,
			MEDIUM,
			HIGH,
		}

		/* Collection option */
		@Arg
		List<String> filenames = new ArrayList<>();

		/* Array option */
		@Arg
		String[] names;

		@Arg
		Collection<Integer> primes;

		/* enum option, case is ignored */
		@Arg
		Priority priority;

		/* Array of enums */
		@Arg
		Priority[] priorities;

		/* setter based option; see private setFilename() */
		String binary;

		/* Setter methods as options work fine as well */
		@Arg(name = "binary")
		private void setBinaryFileNames(String file) {
			this.binary = file;
		}

	}

	public static void main(String[] ignored) {

		// Supply arguments for example consistancy
		final String[] argv = new String[] {

				/* long options */
				"--binary=binary1",

				"--filenames=filename1",
				"--filenames=filename2,f3,f4,f5",

				"--names=John,Peter",
				"--names=Julia,Katie",

				"--priority=low",
				"--priorities=medium,high",

				"--primes=3,5,7",
				"--primes=11",
		};

		try {
			/* Our option state container. Each field defines an option */
			final CmdlineState cmdlineState = new CmdlineState();

			/* Setup and parse the command line arguments */
			final Args args = Args.of(argv, cmdlineState);

			/* Print out the state object fields */
			System.out.println("-- Example 5 - matched options:");
			args.getMatchedOptions().forEach(System.out::println);

			System.out.println("Unmatched options:");
			args.getUnmatchedOptions().forEach(System.out::println);

			assert cmdlineState.binary.equals("binary1");

			/* Anything that was unmatched by the parser is stored here */
			System.out.println("Unmatched args:");
			args.getUnmatchedArgs().forEach(System.out::println);
			/* etc... */

		} catch (UnrecognizedArgException e) {
			System.err.printf("Error: %s%n", e.getMessage());
			printUsage();

			System.exit(1);
		} catch (InvalidArgException e) {
			System.err.printf("Error: %s%n", e.getMessage());
			printUsage();

			System.exit(1);

		} catch (OptionException e) {
			System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
			System.exit(2);
		}
	}

	private static void printUsage() {
		System.err.println("Usage: Example5"
				+ " --binary binary1"
				+ " [--filenames files[,...]]"
				+ " [--names name[,...]]"
				+ " [--priority lo|medium|high]"
				+ " [--priorities <priority>[,...]]"
				+ " [--primes prime_number[,...]]"
				+ "");
	}
}
