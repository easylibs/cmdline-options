package org.easylibs.options.parser;

public class SyntaxError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470353031989199699L;
	private final int lineno;
	private final int charno;
	private final String line;

	public SyntaxError(String message, int lineno, int charno, String line) {
		super(String.format("%s %d:%d: %s", message, lineno, charno, line));

		this.lineno = lineno;
		this.charno = charno;
		this.line = line;
	}

	public int getLineno() {
		return lineno;
	}

	public int getCharno() {
		return charno;
	}

	public String getLine() {
		return line;
	}

}
