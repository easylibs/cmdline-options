package org.easylibs.options.graph;

import java.util.Optional;
import java.util.function.Predicate;

import org.easylibs.experimental.HasOptionName;

public class LeafNode implements Node {

	BranchNode parent;
	private String name;

	public LeafNode() {
	}

	public LeafNode(String name) {
		this.name = name;
	}

	public Node parent() {
		return parent;
	}

	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "LeafNode ["
				+ ", name=" + name
				+ "]";
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isBranch() {
		return false;
	}

	protected <N extends HasOptionName> Optional<N> findDown(Class<N> type, String name) {

		final Predicate<N> filter = n -> {
			return true
					&& (n instanceof HasOptionName)
					&& ((HasOptionName) n).name().equals(name);
		};

		return findDown(type, filter);
	}

	protected <N> Optional<N> findDown(Class<N> type) {
		return (parent == null) ? Optional.empty() : parent.findDown(type, n -> true);
	}

	protected <N> Optional<N> findDown(Class<N> type, Predicate<N> filter) {
		return (parent == null) ? Optional.empty() : parent.findDown(type, filter);
	}

	public void onAttach() {
	}
}