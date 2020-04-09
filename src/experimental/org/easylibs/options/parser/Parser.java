package org.easylibs.options.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.graph.CompoundNode;
import org.easylibs.options.graph.DialectNode;
import org.easylibs.options.graph.FormatNode;
import org.easylibs.options.graph.GraphNode;
import org.easylibs.options.graph.GroupNode;
import org.easylibs.options.graph.LeafNode;
import org.easylibs.options.graph.OptionNode;
import org.easylibs.options.graph.TextNode;

public final class Parser implements AutoCloseable {

	public static Parser of(File file) throws FileNotFoundException {
		return of(new FileInputStream(file));
	}

	public static Parser of(String definition) throws FileNotFoundException {
		return of(new ByteArrayInputStream(definition.getBytes(StandardCharsets.UTF_8)));
	}

	public static Parser of(InputStream in) {
		return new Parser(new Tokenizer(in));
	}

	public static Parser of(Tokenizer tokenizer) {
		return new Parser(tokenizer);
	}

	private final Tokenizer tokenizer;

	private Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	private Token consume() throws IOException, SyntaxError {
		return tokenizer.token();
	}

	public GraphNode graph() throws IOException, SyntaxError {

		GraphNode graph = new GraphNode();
		while (!lt(0, TokenType.EOF)) {
			graph.addNode(statement());
		}

		return graph;
	}

	private LeafNode statement() throws IOException, SyntaxError {

		if (lt(0, TokenType.WORD, "text")) {
			return text();
		}

		if (lt(0, TokenType.WORD, "group")) {
			return group();
		}

		if (lt(0, TokenType.WORD, "format")) {
			return format();
		}

		if (lt(0, TokenType.WORD, "dialect")) {
			return dialect();
		}

		throw new SyntaxError("invalid statement ", peek().lineno, peek().charno, peek().line);
	}

	public FormatNode format() throws IOException, SyntaxError {
		if (lt(0, TokenType.WORD, "format") && lt(1, TokenType.WORD) && lt(2, TokenType.TEXT)) {
			skip(1);

			return new FormatNode(consume().value, consume().value);
		}

		if (lt(0, TokenType.WORD, "format") && lt(1, TokenType.WORD)) {
			skip(1);

			return new FormatNode(consume().value, null);
		}

		throw new SyntaxError("invalid format option ", peek().lineno, peek().charno, peek().line);
	}

	public CompoundNode compound() throws IOException, SyntaxError {
		if (lt(0, TokenType.WORD, "compound") && lt(1, TokenType.TEXT) && lt(2, TokenType.TEXT)) {
			skip(1);

			return new CompoundNode(consume().value, consume().value);
		}

		if (lt(0, TokenType.WORD, "compound") && lt(1, TokenType.TEXT)) {
			skip(1);

			return new CompoundNode(consume().value);
		}

		throw new SyntaxError("invalid format for compound-option ", peek().lineno, peek().charno, peek().line);
	}

	public DialectNode dialect() throws IOException, SyntaxError {
		if (lt(0, TokenType.WORD, "dialect") && lt(1, TokenType.WORD) && lt(2, TokenType.TEXT)) {
			skip(1);

			final String dialect = peek().value;
			if (!Dialect.isNameValid(dialect)) {
				throw new SyntaxError("invalid dialect option ", peek().lineno, peek().charno, peek().line);
			}

			return new DialectNode(consume().value, consume().value);
		}

		if (lt(0, TokenType.WORD, "dialect") && lt(1, TokenType.WORD)) {
			skip(1);

			final String dialect = peek().value;
			if (!Dialect.isNameValid(dialect)) {
				throw new SyntaxError("invalid dialect option ", peek().lineno, peek().charno, peek().line);
			}

			return new DialectNode(consume().value, null);
		}

		throw new SyntaxError("invalid dialect option ", peek().lineno, peek().charno, peek().line);
	}

	public GroupNode group() throws IOException, SyntaxError {

		if (lt(0, TokenType.WORD, "group") && lt(1, TokenType.TEXT) && lt(2, TokenType.COPEN)) {

			skip(1);
			final GroupNode group = new GroupNode(consume().value);

			groupBlock().forEach(n -> group.addNode(n));

			return group;
		}

		throw new SyntaxError("invalid group block", peek().lineno, peek().charno, peek().line);
	}

	public List<LeafNode> groupBlock() throws IOException, SyntaxError {

		List<LeafNode> list = new ArrayList<>();

		if (lt(0, TokenType.COPEN)) {
			skip(1); // Skip {

			while (!lt(0, TokenType.EOF)) {

				if (lt(0, TokenType.CCLOSE)) {
					skip(1); // Skip }

					return list;
				}

				list.add(groupChild());
			}

			throw new SyntaxError("missing closing block bracket ", peek().lineno, peek().charno, peek().line);
		}

		list.add(groupChild());
		return list;
	}

	public LeafNode groupChild() throws IOException, SyntaxError {

		if (lt(0, TokenType.WORD, "group")) {
			return group();
		}

		if (lt(0, TokenType.WORD, "text")) {
			return text();
		}

		if (lt(0, TokenType.WORD, "option") || lt(0, TokenType.TEXT)) {
			return option();
		}

		if (lt(0, TokenType.WORD, "format")) {
			return format();
		}

		if (lt(0, TokenType.WORD, "compound")) {
			return compound();
		}

		if (lt(0, TokenType.WORD, "dialect")) {
			return dialect();
		} // TODO Auto-generated method stub

		throw new SyntaxError("invalid group child ", peek().lineno, peek().charno, peek().line);
	}

	private boolean lt(int i, TokenType type) throws IOException, SyntaxError {
		return tokenizer.peek(i).type == type;
	}

	private boolean lt(int i, TokenType... type) throws IOException, SyntaxError {

		Token token = tokenizer.peek(i);
		for (TokenType t : type) {
			if (token.type == t) {
				return true;
			}
		}

		return false;
	}

	private boolean lt(int i, TokenType type, String value) throws IOException, SyntaxError {
		final Token token = tokenizer.peek(i);
		return (token.type == type) && token.value.equals(value);
	}

	public OptionNode<?> option() throws IOException, SyntaxError {

		if (lt(0, TokenType.WORD, "option") && lt(1, TokenType.TEXT)) {
			skip(1);

			OptionNode<?> option = new OptionNode<>(consume().value);
			optionBlock().forEach(n -> option.addNode(n));

			return option;
		}

		if (lt(0, TokenType.TEXT)) {
			OptionNode<?> option = new OptionNode<>(consume().value);
			optionBlock().forEach(n -> option.addNode(n));

			return option;
		}

		throw new SyntaxError("invalid option definition", peek().lineno, peek().charno, peek().line);
	}

	public List<LeafNode> optionBlock() throws IOException, SyntaxError {
		List<LeafNode> list = new ArrayList<>();

		if (lt(0, TokenType.COPEN)) {
			skip(1); // Skip {

			while (!lt(0, TokenType.EOF)) {

				if (lt(0, TokenType.CCLOSE)) {
					skip(1); // Skip }

					return list;
				}

				list.add(optionChild());
			}

			throw new SyntaxError("missing closing option bracket ", peek().lineno, peek().charno, peek().line);
		}

		if (!lt(0, TokenType.TEXT, TokenType.CCLOSE)) {
			list.add(optionChild());
		}

		return list;

	}

	public LeafNode optionChild() throws IOException, SyntaxError {
		if (lt(0, TokenType.WORD, "format")) {
			return format();
		}

		if (lt(0, TokenType.WORD, "dialect")) {
			return dialect();
		}

		throw new SyntaxError("invalid option child ", peek().lineno, peek().charno, peek().line);
	}

	private Token peek() throws IOException, SyntaxError {
		return tokenizer.peek(0);
	}

	private void skip(int n) throws IOException, SyntaxError {
		while (n-- > 0) {
			consume();
		}
	}

	public TextNode text() throws IOException, SyntaxError {
		if (lt(0, TokenType.WORD, "text") && lt(1, TokenType.TEXT) && lt(2, TokenType.TEXT)) {
			skip(1);

			return new TextNode(consume().value, consume().value);
		}

		if (lt(0, TokenType.WORD, "text") && lt(1, TokenType.TEXT)) {
			skip(1);

			return new TextNode(consume().value, "");
		}

		throw new SyntaxError("invalid text block", peek().lineno, peek().charno, peek().line);
	}

	@Override
	public void close() throws IOException {
		tokenizer.close();
	}

}
