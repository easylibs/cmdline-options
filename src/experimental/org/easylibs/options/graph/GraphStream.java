package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.easylibs.experimental.ComplexOption;

public class GraphStream extends AbstractSpliterator<ComplexOption<?>> implements GraphVisitor {

	public static Stream<ComplexOption<?>> options(GraphNode graph) {
		return StreamSupport.stream(new GraphStream(graph), false);
	}

	private final ArrayList<BranchNode> branches = new ArrayList<>();
	private List<LeafNode> nodes;
	private int index;

	private GraphStream(GraphNode graph) {
		super(0, 0);
		graph.visit(this);
	}

	@Override
	public void visit(Node node) {
	}

	@Override
	public boolean tryAdvance(Consumer<? super ComplexOption<?>> action) {

		while (true) {

			if (nodes == null || (index >= nodes.size())) {

				if (branches.isEmpty()) {
					break;
				}

				nodes = branches.remove(0).nodes;
				index = 0;
			}

			while (index < nodes.size()) {

				LeafNode node = nodes.get(index++);

				if (node instanceof OptionNode<?>) {
					action.accept((ComplexOption<?>) node);

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void visit(BranchNode node) {

		if (node instanceof GroupNode) {
			branches.add(node);
		}

		GraphVisitor.super.visit(node);
	}

}
