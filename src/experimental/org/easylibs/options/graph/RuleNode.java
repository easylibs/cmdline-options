package org.easylibs.options.graph;

import org.easylibs.experimental.Rule;

public class RuleNode extends LeafNode {

	private final Rule rule;
	private final String debugString;

	public RuleNode(Rule rule) {
		super();
		this.rule = rule;
		this.debugString = "";
	}

	public RuleNode(Rule rule, String expression) {
		super();
		this.rule = rule;
		this.debugString = expression;
	}

	@Override
	public String toString() {
		return "RuleNode [expression=" + debugString + "]";
	}

}
