package org.easylibs.options.graph;

import java.util.List;
import java.util.function.Consumer;

public interface GraphVisitor {

	static GraphVisitor of(Consumer<Node> action) {
		return action::accept;
	}

	void visit(Node node);

	default void incLevel(BranchNode node) {
	}

	default void decLevel(BranchNode node) {
	}

	default boolean abort() {
		return false;
	}

	default void visit(LeafNode node) {
		if (abort()) {
			return;
		}

		visit((Node) node);
	}

	default void visit(BranchNode node) {
		if (abort()) {
			return;
		}

		visit((Node) node);

		incLevel(node);
		visit(node.nodes);
		decLevel(node);
	}

	default void visit(List<? extends LeafNode> nodes) {
		if (nodes == null || abort()) {
			return;
		}

		/* To avoid concurrent modification exception we must iterate old fashion way */
		for (int i = 0; i < nodes.size(); i++) {
			if (abort()) {
				return;
			}

			nodes.get(i).visit(this);
		}

	}

	default void visit(CompoundNode node) {
		if (abort()) {
			return;
		}

		visit((LeafNode) node);
	}

	default void visit(OptionNode<?> node) {
		if (abort()) {
			return;
		}

		visit((BranchNode) node);
	}

	default void visit(GroupNode node) {
		if (abort()) {
			return;
		}

		visit((BranchNode) node);
	}

	default void visit(GraphNode node) {
		if (abort()) {
			return;
		}

		visit((BranchNode) node);
	}

}
