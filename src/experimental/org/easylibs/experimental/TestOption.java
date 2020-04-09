package org.easylibs.experimental;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.easylibs.options.graph.Graph;
import org.junit.Test;

public class TestOption {

	public static String field2;
	public String field3;

	public void setMethod4(String value) {
		throw new UnsupportedOperationException();
	}

	public static void setMethod5(String value) {
		throw new UnsupportedOperationException();
	}

	@Test
	void rawPrimitiveOptions() throws NoSuchOptionException {
		final String[] args = {
				"-opt1",
				"value1",

				"-opt2",
				"value2",

				"-opt3",
				"1003",
		};

		Graph graph = Graph.of();

		Group group1 = Group.of("group1");

		ComplexOption<String> opt1 = group1.add(ComplexOption.of("opt1", String.class));
		ComplexOption<String> opt2 = group1.add(ComplexOption.of("opt2", String::valueOf));

		ComplexOption<Integer> opt3 = ComplexOption.of("opt3", Integer::parseInt);
		ComplexOption<?> opt4 = ComplexOption.of("opt4", Integer::parseInt, Long::parseLong);
		ComplexOption<List<String>> opt5 = ComplexOption.ofList("opt5", String.class);

	}

	@Test
	void rawBeanOptions() throws NoSuchOptionException, SecurityException, NoSuchFieldException {
		final String[] args = {

		};

		final TestOption obj = new TestOption();

		BeanOption opt2 = BeanOption.ofField("field2", TestOption.class);
		BeanOption opt3 = BeanOption.ofField("field3", obj);
		BeanOption opt4 = BeanOption.ofMethod("setMethod4", TestOption.class, String.class);

	}

	void graph() {

		Graph graph = Graph.of();

		Group overall = graph.add(Group.of("Overall Options"));
		overall.add(ComplexOption.fromDefinition("-c"));
		overall.add(ComplexOption.fromDefinition("-S"));
		overall.add(ComplexOption.fromDefinition("-E"));
		overall.add(ComplexOption.fromDefinition("-o file"));
		overall.add(ComplexOption.fromDefinition("-x language"));
		overall.add(ComplexOption.fromDefinition("-v"));
		overall.add(ComplexOption.fromDefinition("-###"));
		overall.add(ComplexOption.fromDefinition("-help[=class[,...]]"));
		overall.add(ComplexOption.fromDefinition("--target-help"));
		overall.add(ComplexOption.fromDefinition("--version"));
		overall.add(ComplexOption.fromDefinition("-pass-exit-codes"));

		Group clang = graph.add(Group.of("C Language Options"));
		clang.add(ComplexOption.fromDefinition("-ansi"));
		clang.add(ComplexOption.fromDefinition("-std=standard"));
		clang.add(ComplexOption.fromDefinition("-fgnu89-inline"));
		clang.add(ComplexOption.fromDefinition("-fpermitted-flt-eval-methods=standard"));
		clang.add(ComplexOption.fromDefinition("-aux-info filename"));

		Group cpplang = graph.add(Group.of("C++ Language Options"));
		cpplang.add(ComplexOption.fromDefinition("fabi-version=n"));
		cpplang.add(ComplexOption.fromDefinition("-fno-access-control"));
		cpplang.add(ComplexOption.fromDefinition("-faligned-new=n"));
		cpplang.add(ComplexOption.fromDefinition("-fcheck-new"));
		cpplang.add(ComplexOption.fromDefinition("-fargs-in-order=n {n:int}"));

		final String[] args = {
				"-opt1",
				"value1",

				"-opt2",
				"value2",

				"-opt3",
				"1003",
		};

	}

	void gccGraph() throws FileNotFoundException {
		final String[] args = {
				"-opt1",
				"value1",

				"-opt2",
				"value2",

				"-opt3",
				"1003",
		};

		Graph gccGraph = Graph
				.fromSpec(new FileInputStream(new File("gcc.options")));

//		gccGraph
//				.option("-c", opt -> System.out.println("" + opt.name()))
//				.option("-o", System.out::println)
//				.group("Overall Options", System.out::println);

	}
}
