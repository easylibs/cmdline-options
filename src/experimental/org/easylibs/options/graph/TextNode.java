package org.easylibs.options.graph;

public class TextNode extends LeafNode {

	public String name;
	public String value;

	public TextNode() {
		super();
	}

	public TextNode(String name, String value) {
		super();

		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Text [name='" + name + "', value='" + value.trim() + "']";
	}

}
