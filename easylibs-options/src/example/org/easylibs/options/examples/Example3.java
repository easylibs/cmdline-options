package org.easylibs.options.examples;

import java.util.List;
import java.util.Optional;

import org.easylibs.options.Args;
import org.easylibs.options.InvalidArgException;
import org.easylibs.options.Option;
import org.easylibs.options.Options;
import org.easylibs.options.UnrecognizedArgException;

public class Example3 {

	public static void main(String[] ignored) {
		
		// Supply arguments for example consistancy
		final String[] argv = new String[] {

				/* short options */
				"-ab",
				"dd-argument",
				"-0",
				"-1",
				"-2",

				/* long options */
				"--add=123",
				"--append",
				"--delete-float=3.141952",
				"--delete-double=3.141952",
				"--verbose",
				"--create=789",
				"-c987",
				"--file=filename1",

				/* non-option arguments */
				"--",
				"hello",
				"there",
		};

		try {

			/* Two ways to build options, via create() method or using() */
			final Options options = new Options.Builder()

					/* Short options */
					.create("a")
					.create("b")
					.create("c", int.class)
					.create("d", String.class)
					.create("0")
					.create("1")
					.create("2")

					.group('o', suboptions -> suboptions
							.create("ro")
							.create("rw")
							.create("name", String.class))

					/* Long options */
					.using(Option.of("add", int.class))
					.using(Option.of("append"))
					.using(Option.of("delete-float", float.class))
					.using(Option.of("delete-double", double.class))
					.using(Option.of("verbose"))
					.using(Option.of("create", int.class))
					.using(Option.of("file", String.class))
					.build();

			/* Handler be notified when option is matched */
			final Option<Integer> create = options.get("create", int.class);
			create.onMatch(n -> System.out.printf("Creating new entry %d%n", n));

			/* Setup and parse the command line arguments */
			final Args args = Args.of(argv, options);

			/* All matched options */
			System.out.println("-- Example 3:");
			System.out.println("Matched options:");
			final List<Option<?>> matched = options.getAllMatched();
			matched.forEach(System.out::println);

			/* Anything that was unmatched by the parser is stored here */
			System.out.println("Unmatched args:");
			final List<String> unmatched = args.getUnmatchedArgs();
			unmatched.forEach(System.out::println);

			/* 2 ways to work with user options: getOption or findMatched */

			/*
			 * Find methods always return a java Optional. Unmatched option with no command
			 * line match, the returned optional will be empty, otherwise the Optional will
			 * have the option with the commanline arg/value.
			 */
			final Optional<Option<Boolean>> a = options.findMatched("a", boolean.class);
			a.ifPresent(opt -> System.out.println("Option A is found!"));

			/* Get method will throw unchecked OptionNotFoundException if not found */
			final Option<Integer> add = options.get("add", int.class);
			if (add.isMatched()) {
				System.out.printf("add this amount %d%n", add.getValue());
			}

		} catch (UnrecognizedArgException | InvalidArgException e) {
			System.err.println("Error: " + e.getMessage());
			System.err.printf("Usage: %s [-a|-b|-0|-1|-2] [-c arg] [-d arg]%n", "Example3");
			System.err.printf("      "
					+ " [--add element]"
					+ " [--append]"
					+ " [--delete-float arg]"
					+ " [--delete-double arg]"
					+ " [--verbose]"
					+ " [--create arg]"
					+ " [--file arg]"
					+ "%n");
			System.exit(1);
		}

		/*
		 * Next step: Example 4 - working with beans
		 * 
		 * A much easiest way to deal with many options is using the BeanOption API.
		 * Example 4 demostrates its usage.
		 */
	}
}
