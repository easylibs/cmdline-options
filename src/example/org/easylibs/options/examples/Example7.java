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

import java.util.Optional;
import java.util.Properties;

import org.easylibs.options.Args;
import org.easylibs.options.OptionException;

/**
 * The Class Example7 shows use of {@code PropertiesArgs} and implicit error
 * handler.
 * 
 * Output:
 * 
 * <pre>
-- Example 7 - matched options:
Option [name='write-on-exit', arg-optional, matches=1, Properties [write-on-exit], type=String]
Option [name='filenames', value='f1,f2,f3,f4,f5', arg-optional, matches=1, Properties [filenames], type=String]

Unmatched options:

Example 7: Properties={write-on-exit=1, filenames=f1,f2,f3,f4,f5}
 * </pre>
 */
public class Example7 {

	public static void main(String[] ignored) throws OptionException {

		// Supply arguments for example consistancy
		final String[] argv = new String[] {
				"--filenames=f1,f2,f3,f4,f5",
				"--write-on-exit",
		};

		final Properties properties = new Properties();
		properties.setProperty("filenames", "");
		properties.setProperty("write-on-exit", "");

		/*
		 * Implicit error handler is Args::printError, Optional is empty on any errors
		 */
		final Optional<Args> argsOptional = Args.ofOptional(argv, properties);

		if (argsOptional.isPresent()) {
			final Args args = argsOptional.get();

			/* Print out the state object fields */
			System.out.println("-- Example 7 - matched options:");
			args.getMatchedOptions().forEach(System.out::println);
			System.out.println();

			System.out.println("Unmatched options:");
			args.getUnmatchedOptions().forEach(System.out::println);
			System.out.println();

			System.out.printf("Example 7: Properties=" + properties);
		}
	}

}
