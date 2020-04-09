/*
 * 
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
