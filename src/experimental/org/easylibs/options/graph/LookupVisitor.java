package org.easylibs.options.graph;

import java.util.Optional;
import java.util.function.Predicate;

import org.easylibs.experimental.ComplexOption;

public class LookupVisitor implements GraphVisitor {

	private Predicate<ComplexOption<?>> predicate;
	private OptionNode<?> match;
	private final GraphNode graph;

	public LookupVisitor(GraphNode graph) {
		this.graph = graph;
	}

	public Optional<ComplexOption<?>> option(String name) {
		return option(o -> o.name().equals(name));
	}

	public Optional<ComplexOption<?>> option(Predicate<ComplexOption<?>> predicate) {
		this.predicate = predicate;
		this.match = null;

		graph.visit(this);

		return Optional.ofNullable(match);
	}

	@Override
	public boolean abort() {
		return match != null || predicate == null;
	}

	@Override
	public void visit(Node node) {
	}

	@Override
	public void visit(OptionNode<?> option) {

		if (predicate.test(option)) {
			this.match = option;
		}

	}

}
