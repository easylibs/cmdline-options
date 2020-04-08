package org.easylibs.options.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Tokenizer implements AutoCloseable {

	private Token EOF_TOKEN;

	private final BufferedReader in;
	private final List<Token> stack = new ArrayList<>();

	public Tokenizer(InputStream in) {
		this(new BufferedReader(new InputStreamReader(in)));
	}

	public Tokenizer(BufferedReader in) {
		this.in = in;

		skipWhitespace(true);
	}

	public Token peek(int index) throws IOException, SyntaxError {

		if (stack.size() <= index) {
			int needed = index - stack.size() + 1;
			if (fetchTokens(needed) < needed) {
				return EOF_TOKEN;
			}
		}

		return stack.get(index);
	}

	public Token token() throws IOException, SyntaxError {
		if (stack.isEmpty()) {
			if (fetchTokens(1) != 1) {
				return EOF_TOKEN;
			}
		}

		return stack.remove(0);
	}

	private String line;
	private char[] buffer;
	private int lineno;
	private int charno;
	private boolean isClosed;

	private boolean skipWhitespace;

	public void skipWhitespace(boolean b) {
		this.skipWhitespace = b;
	}

	private int fetchTokens(int count) throws IOException, SyntaxError {
		if (!isClosed && count <= 0) {
			return 0;
		}

		int cnt = 0;

		while (!isClosed && (cnt < count) && (fetchSingleToken() != TokenType.EOF)) {
			cnt++;
		}

		return cnt;
	}

	private TokenType fetchSingleToken() throws IOException, SyntaxError {

		if (isClosed || fetchNonEmptyLine() == null) {
			return TokenType.EOF;
		}

		Token token = null;

		while (((token = readToken()).type == TokenType.WHITESPACE) && skipWhitespace)
			;

		stack.add(token);

		return token.type;
	}

	private Token readToken() throws SyntaxError, IOException {
		final StringBuilder b = new StringBuilder();

		TokenType type = null;
		int charstart = charno;

		LOOP: while (charno < buffer.length) {
			final char ch = buffer[charno];

			switch (ch) {
			case '\n':
			case ' ':
			case '\t':
				if (type != null && type != TokenType.WHITESPACE) {
					break LOOP;
				}

				type = TokenType.WHITESPACE;
				charno++;
				b.append(ch);
				break;

			case '"':
				if (type != null) {
					break LOOP;
				}

				charstart = charno;
				return new Token(TokenType.TEXT, readText(), lineno, charstart, line);

			case '{':
				if (type != null) {
					break LOOP;
				}

				return new Token(TokenType.COPEN, "{", lineno, charno++, line);

			case '}':
				if (type != null) {
					break LOOP;
				}

				return new Token(TokenType.CCLOSE, "}", lineno, charno++, line);

			default:
				if (type != null && type != TokenType.WORD) {
					break LOOP;
				}

				type = TokenType.WORD;
				charno++;
				b.append(ch);
				break;
			}
		}

		return new Token(type, b.toString(), lineno, charstart, line);
	}

	private String readTrippleQuoteText() throws SyntaxError, IOException {
		if (!((charno + 3) <= buffer.length)
				&& (buffer[charno + 0] == '"')
				&& (buffer[charno + 1] == '"')
				&& (buffer[charno + 2] == '"')) {
			throw new SyntaxError("invalid text block string, missing opening tripple quote", lineno, charno, line);
		}

		charno += 3;
		final StringBuilder b = new StringBuilder();

		int endIndex;

		while ((endIndex = line.indexOf("\"\"\"", charno)) == -1) {
			final String str = line.substring(charno);
			charno += str.length();

			b.append(str);

			if (fetchLine() == null) {
				throw new SyntaxError("invalid text block string, missing closing tripple quote", lineno, charno, line);
			}
		}

		final String str = line.substring(charno, endIndex);
		b.append(str);
		charno += str.length() + 3;

		return b.toString();
	}

	private String readText() throws SyntaxError, IOException {

		if (((charno + 3) <= buffer.length)
				&& (buffer[charno + 0] == '"')
				&& (buffer[charno + 1] == '"')
				&& (buffer[charno + 2] == '"')) {
			return readTrippleQuoteText();
		}

		if (buffer[charno++] != '"') {
			throw new SyntaxError("invalid double-quote string", lineno, charno, line);
		}

		final StringBuilder b = new StringBuilder();

		while (charno < buffer.length) {
			char ch = buffer[charno++];
			if (ch == '"') {
				return b.toString();
			}

			b.append(ch);
		}

		throw new SyntaxError("double-quote not terminated", lineno, charno, line);
	}

	private void clearLine() {
		buffer = null;
		line = null;
	}

	private String fetchNonEmptyLine() throws IOException {
		while (fetchLine() != null && line.trim().isEmpty()) {
			charno += line.length();
		}

		return line;
	}

	private String fetchLine() throws IOException {
		if (isClosed) {
			return null;
		}

		if (line == null || (charno == buffer.length)) {

			clearLine();

			line = in.readLine();
			lineno++;
			charno = 0;

			if (line == null) {
				isClosed = true;
				stack.add(EOF_TOKEN = new Token(TokenType.EOF, "", lineno, charno, line));

				return null;
			}

			buffer = line.toCharArray();
		}

		return line;
	}

	@Override
	public void close() throws IOException {
		in.close();
	}
}
