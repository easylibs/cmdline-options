/*
 * 
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
