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

import java.util.List;
import java.util.Optional;

import org.easylibs.options.Arg;
import org.easylibs.options.Args;
import org.easylibs.options.OptionException;

/**
 * The Class Example6.
 * 
 * <pre>
-- Example 6 - matched options:
Option [name='filenames', value='[f1, f2, f3, f4, f5]', matches=1, arg-required, Bean [field=Example6::filenames], type=List]

Unmatched options:
Option [name='option2', Bean [field=Example6::option2], type=boolean]

Unmatched args:
--
random string
 * </pre>
 */
public class Example6 {

	/* Collection option */
	@Arg
	private static List<String> filenames;

	@Arg
	private volatile static boolean option2;

	public static void main(String[] ignored) throws OptionException {

		// Supply arguments for example consistancy
		final String[] argv = new String[] {
				"--filenames=f1,f2,f3,f4,f5",
				"--",
				"random string"
		};

		/*
		 * The ofOptional() method only returns the Args parser on success, otherwise it
		 * calls the default error handler and returns an empty value.
		 */
		final Optional<Args> argsOptional = Args.ofOptional(argv, Example6.class);

		if (argsOptional.isPresent()) {
			final Args args = argsOptional.get();

			/* Print out the state object fields */
			System.out.println("-- Example 6 - matched options:");
			args.getMatchedOptions().forEach(System.out::println);
			System.out.println();

			System.out.println("Unmatched options:");
			args.getUnmatchedOptions().forEach(System.out::println);
			System.out.println();

			/* Anything that was unmatched by the parser is stored here */
			System.out.println("Unmatched args:");
			args.getUnmatchedArgs().forEach(System.out::println);
			System.out.println();
		}
	}
}
