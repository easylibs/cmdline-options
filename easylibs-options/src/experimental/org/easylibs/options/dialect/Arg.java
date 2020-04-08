package org.easylibs.options.dialect;

import java.util.List;

public class Arg {

	private final Dialect dialect;
	private final List<String> args;
	private final int index;
	private final String value;

	public Arg(Dialect dialect, List<String> args, int index, String value) {
		this.dialect = dialect;
		this.args = args;
		this.index = index;
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return "Arg [value=" + value + ", index=" + index + "]";
	}

}
