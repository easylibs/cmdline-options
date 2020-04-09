package org.easylibs.options.parser;

public class Token {

	public final TokenType type;
	public final String value;
	public final int lineno;
	public final int charno;
	public final String line;

	public Token(TokenType type, String value, int lineno, int charno, String line) {
		this.type = type;
		this.value = value;
		this.lineno = lineno;
		this.charno = charno;
		this.line = line;
	}

	@Override
	public String toString() {
		return "Token [type=" + type + ", value=" + value + ", lineno=" + lineno + ", charno=" + charno + ", line="
				+ line + "]";
	}

}
