package org.easylibs.options.graph;

import java.io.PrintStream;
import java.util.function.BiConsumer;

public abstract class PrintVisitor implements GraphVisitor {

	public static void println(Node node, PrintVisitor formatter) {

		final boolean b = node.isBranch() && !((BranchNode) node).nodes.isEmpty();

		formatter.indent(b ? "+ " : "- ").println(node.toString());
	}

	public static PrintVisitor of(BiConsumer<Node, PrintVisitor> action) {
		return new PrintVisitor() {

			@Override
			public void visit(Node node) {
				action.accept(node, this);
			}
		};
	}

	int level;
	private PrintStream out;

	public PrintVisitor() {
		this(System.out);
	}

	public PrintVisitor(PrintStream out) {
		this.out = out;
	}

	public String pad() {
		final String pad = "                                   "
				.substring(0, level * 2)
//				+ "+ "

		;

		return pad;
	}

	public PrintStream indent(String postfix) {
		out.print(pad() + postfix);

		return out;
	}

	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub

	}

	public int level() {
		return this.level;
	}

	@Override
	public void incLevel(BranchNode node) {

		this.level++;
	}

	@Override
	public void decLevel(BranchNode node) {
		this.level--;

	}

}
