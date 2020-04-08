package org.easylibs.options.graph;

import java.util.Optional;

import org.easylibs.options.dialect.Dialect;

public class DialectNode extends LeafNode implements Dialect {

	private String name;
	private String format;
	private Dialect dialect;

	public boolean isOption(String arg) {
		return dialect.isOption(arg);
	}

	public boolean isOptionMatch(String arg, String undecorated) {
		return dialect.isOptionMatch(arg, undecorated);
	}

	public DialectNode(String name, String format) {
		super();
		this.name = name;

		this.format = format;
		this.dialect = Dialect.of(name, format);
	}

	@Override
	public String toString() {
		if (format == null) {
			return "Dialect [name=" + name + "]";

		} else {
			return "Dialect [name='" + name + "', format='" + format + "']";
		}
	}

	@Override
	public String optionName(String definition) {
		return dialect.optionName(definition);
	}

	@Override
	public Optional<String> valueName(String definition) {
		return dialect.valueName(definition);
	}

}
