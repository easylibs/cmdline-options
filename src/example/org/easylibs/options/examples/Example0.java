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
import org.easylibs.options.InvalidArgException;
import org.easylibs.options.Option;
import org.easylibs.options.Options;
import org.easylibs.options.UnrecognizedArgException;

public class Example0 {

	@SuppressWarnings("unused")
	public static void main(String[] ignored) {
		
		// Supply arguments for example consistancy
		final String[] argv = new String[] {
				"--filenames=f1,f2,f3,f4,f5",
				"--write-on-exit",
		};

		System.out.println("-- Example 0:");

		final Properties properties = new Properties();
		properties.setProperty("filenames", "");
		properties.setProperty("write-on-exit", "");

		Option<?> optionA = Option.of("filenames");
		Option<?> optionB = Option.of("write-on-exit");

		try {

			Args a1 = Args.of(argv, Options.of(optionA, optionB));
			Args a2 = Args.of(argv, optionA, optionB);
			Args a3 = Args.of(argv, Example7.class);
			Args a4 = Args.of(argv, new Example7());
			Args a5 = Args.of(argv, properties);

		} catch (UnrecognizedArgException | InvalidArgException e) {
			Args.printError(e);
		}

		Args.setDefaultErrorHandler(Args::printError);

		Optional<Args> b1 = Args.ofOptional(argv, Options.of(optionA, optionB));
		Optional<Args> b2 = Args.ofOptional(argv, Example7.class);
		Optional<Args> b3 = Args.ofOptional(argv, new Example7());
		Optional<Args> b4 = Args.ofOptional(argv, properties);

		Args.ofOptional(argv, properties)
				.ifPresent(args -> args.forEach(System.out::println));
		System.out.println("properties=" + properties);

	}

}
