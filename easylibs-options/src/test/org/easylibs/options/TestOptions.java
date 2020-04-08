package org.easylibs.options;

import java.util.function.Predicate;

import junit.framework.TestCase;

public class TestOptions {

	@Arg
	private float deleteFloat;

	@Arg
	private double deleteDouble;

	private String file;

	@Arg()
	private void setFile(String filename1) {
		this.file = filename1 + ".txt";
	}

//	@Test
	public void lowLevelTest() throws DuplicateOptionException, TypeMapperNotFoundException, UnrecognizedArgException,
			InvalidArgException {
		final String[] argv = {
				/* short options */
				"-abd",
				"d-argument",
				"-0",
				"-1",
				"-2",

				/* long options */
				"--add",
				"123",
				"--append",
				"--delete-float=3.141952",
				"--delete-double=3.141952",
				"--verbose",
				"--create",
				"789",
				"-c",
				"987",
				"--file",
				"filename1",

				/* non-option arguments */
				"--",
				"hello",
				"there",
				"dude",

		};

		final Option<?>[] optionArray = {
				Option.named("0"),
				Option.named("1"),
				Option.named("2"),

				Option.named("a"),
				Option.named("b"),
				Option.named("c", int.class),
				Option.named("d", String.class),
				Option.namedOptional("add", String.class),
				Option.named("append"),
//				BeanOption.of(this, "delete"),
				Option.named("verbose"),
				Option.named("create", String.class),
//				Option.of("file", String.class),
//				BeanOption.of(this, "file"),
		};

		final Options options = new Options.Builder()
				.usingAll(optionArray)
				.build();

		final Args args = Args.of(argv, options);

		System.out.println("Matched options:");
		options.getAllMatched()
				.forEach(System.out::println);

		System.out.println();
		System.out.println("Unmatched options:");
		options.getAllUnmatched()
				.forEach(System.out::println);

		System.out.println();
		System.out.println("Unmatched args: " + args.getUnmatchedArgs());

		TestCase.assertEquals(3.141952f, deleteFloat);
		TestCase.assertEquals(3.141952d, deleteDouble);
		TestCase.assertEquals("filename1.txt", file);

		TestCase.assertTrue(options.find("add", String.class).isPresent());
		TestCase.assertEquals("123", options.find("add", String.class).get().value());
	}

	static boolean b = true;

	static void printe(Throwable e, Predicate<StackTraceElement> filter) {
		e.printStackTrace();

//		Stream.of(e.getStackTrace())
//				.filter(filter)
//				.forEach(System.out::println);
	}

}
