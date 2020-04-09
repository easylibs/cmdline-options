package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BranchNode extends LeafNode implements Node {

	public final List<LeafNode> nodes = new ArrayList<>();

	public BranchNode() {
		super();
	}

	public BranchNode(String name) {
		super(name);
	}

	public synchronized <T extends LeafNode> T addNode(Supplier<T> child) {
		return addNode(child.get());
	}

	public synchronized <T extends LeafNode> T addNode(T child) {
		nodes.add(child);
		child.parent = this;

		return child;
	}

	@Override
	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

	protected <N> Optional<N> findDown(Class<N> type, Predicate<N> filter) {

		@SuppressWarnings("unchecked")
		final Optional<N> result = nodes.stream()
				.filter(n -> type.isAssignableFrom(n.getClass()))
				.map(n -> (N) n)
				.filter(filter::test)
				.findFirst();

		if (!result.isPresent()) {
			return super.findDown(type, filter);
		}

		return result;
	}

	@Override
	public void onAttach() {
		nodes.forEach(n -> n.onAttach());

		super.onAttach();
	}

	@Override
	public boolean isBranch() {
		return true;
	}

}
