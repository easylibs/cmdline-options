package org.easylibs.options.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easylibs.experimental.ComplexOption;
import org.easylibs.options.dialect.DialectMatcher;
import org.easylibs.options.graph.GraphNode;
import org.easylibs.options.graph.GroupNode;
import org.easylibs.options.graph.LinkerVisitor;
import org.easylibs.options.graph.MatchVisitor;
import org.easylibs.options.graph.PrintVisitor;
import org.easylibs.options.graph.RuleNode;
import org.junit.Test;

public class TestParser {

	private final static File FILE1 = new File("test.options");
	private final static File FILE2 = new File("test2.options");

//	@Test
	public void testTokenizer() throws IOException, SyntaxError {

		try (final Tokenizer tokenizer = new Tokenizer(new FileInputStream(FILE1))) {

			Token token = null;

			while ((token = tokenizer.token()).type != TokenType.EOF) {
				System.out.println(token);
			}
		}
	}

//	@Test
	public void testParser() throws FileNotFoundException, IOException, SyntaxError {

		try (final Parser parser = Parser.of(FILE1)) {

			System.out.println(parser.text());
			System.out.println(parser.text());
			System.out.println(parser.text());

			GroupNode group = parser.group();
			group.addNode(new RuleNode((graph, node) -> true, "true"));
			System.out.println(group);

			group.nodes.forEach(System.out::println);
		}

	}

//	@Test
	public void testTestOptions() throws FileNotFoundException, IOException, SyntaxError {

		try (final Parser parser = Parser.of(FILE2)) {

			GraphNode graph = parser.graph();
			graph.visit(PrintVisitor.of(PrintVisitor::println));

		}

	}

	@Test
	public void testMatcher() throws FileNotFoundException, IOException, SyntaxError {
		final String[] args = {
				"-E",
				"-c",
				"-o",
				"some.txt",
				"-x",
				"en",
				"-fabi-version",
				"0010",
				"-fcheck-new",
				"-Wabi-tag",
				"-Wconversion-null",
		};

		System.out.println("args=" + Arrays.asList(args));
		System.out.println();

		try (final Parser parser = Parser.of(FILE2)) {

//			MapperRegistry.global().register(Language.class, String::toUpperCase, Language::valueOf);

			final GraphNode graph = parser.graph();

			final LinkerVisitor linker = new LinkerVisitor(graph);
			linker.link();

			final List<ComplexOption<?>> matchedOptions = new ArrayList<>();
			new MatchVisitor(graph, new DialectMatcher(Arrays.asList(args)))
					.onMatch(System.out::println)
					.onMatch(matchedOptions::add)
//					.onUnmatched(option -> System.out.println("unmatched: " + option))
					.matchAll()

			;

//			matchedOptions.forEach(System.out::println);

//			graph.forEach(System.out::println);
		}

	}
}
