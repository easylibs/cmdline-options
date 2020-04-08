package org.easylibs.options.graph;

public class FormatNode extends LeafNode {

	private String name;
	private String format;

	public FormatNode() {
		super();
	}

	public FormatNode(String name, String format) {
		super();
		this.name = name;

		this.format = format;
	}

	@Override
	public String toString() {
		if (format == null) {
			return "Format [name=" + name + "]";

		} else {
			return "Format [name='" + name + "', format='" + format + "']";
		}
	}

}
