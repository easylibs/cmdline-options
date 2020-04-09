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
package org.easylibs.options.examples;

import org.easylibs.options.Arg;
import org.easylibs.options.Args;
import org.easylibs.options.InvalidArgException;
import org.easylibs.options.OptionException;
import org.easylibs.options.Options;
import org.easylibs.options.UnrecognizedArgException;

public class Example4 {

	/*
	 * Note that we could've set the @Arg annotation on the class type which would
	 * automatically make all fields options. For clarity, we annotated each field
	 * and setter individually.
	 */
	private static class CmdlineState {

		@Arg
		boolean a;

		@Arg
		static boolean b;

		@Arg
		String d;

		@Arg(name = "0")
		boolean zero;

		@Arg(name = "1")
		boolean one;

		@Arg(name = "2")
		boolean two;

		@Arg
		int add;

		@Arg
		boolean append;

		@Arg
		float deleteFloat;

		@Arg
		double deleteDouble;

		@Arg
		boolean verbose;

		@Arg
		int create;

		@Arg
		int c;

		/* This will be ommited, even though its annotated */
		@Arg(ommit = true)
		private transient float myPrivateField;

		@Override
		public String toString() {
			return "CmdlineState [a=" + a + ", b=" + b + ", d=" + d + ", zero=" + zero + ", one=" + one + ", two=" + two
					+ ", add=" + add + ", append=" + append + ", deleteFloat=" + deleteFloat + ", deleteDouble="
					+ deleteDouble + ", verbose=" + verbose + ", create=" + create + ", c=" + c
					+ "]";
		}

	}

	/**
	 * Example entry point
	 *
	 * @param argv ignored, the example uses its own argument array for consistence
	 */
	public static void main(String[] ignored) {
		
		// Supply arguments for example consistancy
		final String[] argv = new String[] {

				/* short options */
				"-abd",
				"d-argument",
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

				/* non-option arguments */
				"--",
				"hello",
				"there",
		};

		/*
		 * In this example we are going to use bean API to store our option values.
		 * Beans extend regular SimpleOption but in addition to setting the value with
		 * the option they also set the value in a container object or class. Each field
		 * or bean setter method corresponds to an option. Annotation Arg is used to
		 * mark and modify certain option attributes as well within the code.
		 */

		try {
			/* Our option state container. Each field defines an option */
			final CmdlineState cmdlineState = new CmdlineState();

			/*
			 * The container cmdlineState is scanned and options are created for each
			 * annotated field
			 */
			final Options options = Options.fromBeans(cmdlineState);

			/* Add a handler that will be notified when a specific option is matched */
			options.get("create", int.class)
					.onMatch(n -> System.out.printf("Creating new entry %d%n", n));

			/* Setup and parse the command line arguments */
			final Args args = Args.of(argv, options);

			/* Print out the state object fields */
			System.out.println("-- Example 4:");
			System.out.println(cmdlineState);

			/* Anything that was unmatched by the parser is stored here */
			System.out.println("Unmatched args:");
			args.getUnmatchedArgs().forEach(System.out::println);

			/* The matched command line option values are set in the bean container */
			assert cmdlineState.d.equals("d-argument");
			assert cmdlineState.a == true;
			assert CmdlineState.b == true; // Static fields work just the same
			assert cmdlineState.c == 987;
			assert cmdlineState.add == 123;
			assert cmdlineState.verbose == true;
			/* etc... */

		} catch (UnrecognizedArgException e) {
			System.err.printf("Error: %s%n", e.getMessage());
			printUsage();

			System.exit(1);
		} catch (InvalidArgException e) {
			System.err.printf("Error: %s%n", e.getMessage());
			printUsage();

//			e.getCause().printStackTrace();

			System.exit(1);

		} catch (OptionException e) {
			System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
			System.exit(2);
		}
	}

	private static void printUsage() {
		System.err.printf("Usage: %s [-a|-b|-0|-1|-2] [-c arg] [-d arg]%n", "Example4");
		System.err.printf("      "
				+ " [--add element]"
				+ " [--append]"
				+ " [--delete arg]"
				+ " [--verbose]"
				+ " [--create arg]"
				+ " [--file arg]"
				+ "%n");

	}
}
