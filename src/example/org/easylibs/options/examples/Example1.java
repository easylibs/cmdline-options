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

import org.easylibs.getopt.PosixGetopt;

public class Example1 {

	public static void main(String[] ignored) {

		// Supply arguments for example consistancy
		final String[] argv = new String[] {
				"-n",
				"-t",
				"200",
				"john",
		};

		System.out.println("-- Example 1:");

		final PosixGetopt options = new PosixGetopt("nt:");
		options.setArgs(argv);

		int flags = 0;
		int nsecs = 0;
		int tfnd = 0;

		char opt;
		while ((opt = options.getopt()) != PosixGetopt.NO_MORE_OPTIONS) {
			switch (opt) {

			case 'n': {
				flags = 1;
				break;
			}

			case 't': {
				nsecs = Integer.parseInt(options.optarg());
				tfnd = 1;
				break;
			}

			default: /* '?' */
				System.err.printf("Usage: %s [-t nsecs] [-n] name%n", "Example1");
				System.exit(1);
			}
		}

		System.out.printf("flags=%d; tfnd=%d; nsecs=%d; optind=%d%n", flags, tfnd, nsecs, options.index());

		if (options.index() >= argv.length) {
			System.err.println("Expected argument after options");
			System.exit(1);
		}

		System.out.printf("name argument = %s%n", argv[options.index()]);

		/*
		 * Next step: Example 2 - long options
		 * 
		 * Short options are great but if you need more expanded capabilities Example 2
		 * shows how to add long-options to getopts.
		 */

	}

}
