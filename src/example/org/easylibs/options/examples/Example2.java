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

import java.util.concurrent.atomic.AtomicReference;

import org.easylibs.getopt.PosixGetopt;
import org.easylibs.getopt.PosixLongOption;
import org.easylibs.getopt.PosixLongOption.HasArg;

public class Example2 {

	public static void main(String[] ignored) {
		
		// Supply arguments for example consistancy
		final String[] argv = new String[] {

				/* short options */
				"-a",
				"-b",
				"-cc-argument",
				"-dd-argument",
				"-0",
				"-1",
				"-2",

				/* long options */
				"--add",
				"123",
				"--append",
				"--delete=456",
				"--verbose",
				"--create",
				"789",
				"--file",
				"filename1",
				"-c",
				"987",

				/* non-option arguments */
				"hello",
				"there",
		};

		/*
		 * We set the long option that was selected on the command line. Instead of
		 * index into options array as specified in the linux getopt(3) version, we
		 * dispatch to a callback which sets the long option in this reference.
		 */
		final AtomicReference<PosixLongOption> selected = new AtomicReference<>();

		final PosixLongOption[] longopts = {
				new PosixLongOption("add", HasArg.REQUIRED),
				new PosixLongOption("append", HasArg.NO),
				new PosixLongOption("delete", HasArg.REQUIRED),
				new PosixLongOption("verbose", HasArg.NO),
				new PosixLongOption("create", HasArg.REQUIRED, 'c'), // With short option
				new PosixLongOption("file", HasArg.REQUIRED),
		};

		System.out.println("-- Example 2:");

		/*
		 * Combine shortopts with long options. Both are matched. On long option match
		 * use the callback interface, to which we supply a lambda method reference.
		 */
		final PosixGetopt options = new PosixGetopt("abc:d:012", longopts);
		options.setArgs(argv);

		char opt;
		while ((opt = options.getopt(selected::set)) != PosixGetopt.NO_MORE_OPTIONS) {
			switch (opt) {

			case PosixGetopt.LONG_OPT: {
				final PosixLongOption loption = selected.get();

				System.out.printf("option '%s'", loption.getName());
				if (loption.getHasArg() != HasArg.NO) {
					System.out.printf(" with arg '%s'", options.optarg());
				}
				System.out.println();

				break;
			}

			case '0':
			case '1':
			case '2': {
				System.out.printf("option '%s'%n", "" + opt);
				break;
			}

			case 'a': {
				System.out.printf("option 'a'%n");
				break;
			}

			case 'b': {
				System.out.printf("option 'b'%n");
				break;
			}

			case 'c': {
				System.out.printf("option 'c' with value '%s'%n", options.optarg());
				break;
			}

			case 'd': {
				System.out.printf("option 'd' with value '%s'%n", options.optarg());
				break;
			}

			default: /* '?' */
				System.err.printf("Usage: %s [-a|-b|-0|-1|-2] [-c arg] [-d arg]%n", "Example2");
				System.err.printf("      "
						+ " [--add element]"
						+ " [--append]"
						+ " [--delete arg]"
						+ " [--verbose]"
						+ " [--create arg]"
						+ " [--file arg]"
						+ "%n");
				System.exit(1);
			}
		}

		/*
		 * index() has the current index within the args array which is the first
		 * unmatched arg
		 */
		if (options.index() < argv.length) {
			System.out.print("non-option ARGV-elements: ");

			/* index(1) method increments the index by 1 into the args array */
			while (options.index() < argv.length) {
				System.out.printf("%s ", argv[options.index(1)]);
			}

			System.out.println();
		}

		/*
		 * Next step: Example 3 - Args and Option API
		 * 
		 * Getopts is low level, but legacy API works great in certain cases, especially
		 * when not dealing with too many options. Example 3 shows how to use a more
		 * modern level API using user defined Option API.
		 */

	}

}
