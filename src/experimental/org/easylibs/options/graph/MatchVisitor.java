package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.easylibs.extoptions.CompoundOption;
import org.easylibs.extoptions.ComplexOption;
import org.easylibs.options.dialect.Match;
import org.easylibs.options.dialect.Matcher;

public class MatchVisitor implements GraphVisitor {

	private final Matcher matcher;

	private List<Consumer<ComplexOption<?>>> onMatchHandlers = new ArrayList<>();
	private List<Consumer<ComplexOption<?>>> onUnmatchedHandlers = new ArrayList<>();
	private List<CompoundOption> compoundOptions = new ArrayList<>();

	private final GraphNode graph;

	public MatchVisitor(GraphNode graph, Matcher matcher) {
		this.graph = graph;
		this.matcher = matcher;
	}

	public MatchVisitor onMatch(Consumer<ComplexOption<?>> onMatch) {
		this.onMatchHandlers.add(onMatch);

		return this;
	}

	public MatchVisitor onUnmatched(Consumer<ComplexOption<?>> onUnmatched) {
		this.onUnmatchedHandlers.add(onUnmatched);

		return this;
	}

	public void matchAll() {
		graph.visit(this);

		compoundOptions.stream()
				.filter(c -> c.isPresent())
				.forEach(c -> onMatchHandlers.forEach(h -> h.accept(c)));
		compoundOptions.stream()
				.filter(c -> !c.isPresent())
				.forEach(c -> onUnmatchedHandlers.forEach(h -> h.accept(c)));
		compoundOptions.clear();
	}

	@Override
	public void visit(Node node) {
	}

	@Override
	public void visit(CompoundNode compound) {
		compoundOptions.add(compound);
	}

	@Override
	public void visit(OptionNode<?> option) {

		final Optional<Match> optionalMatch = matcher.findMatch(option);

		if (optionalMatch.isPresent()) {

			option.setMatch(optionalMatch.get());
			option.compound().ifPresent(compound -> compound.addMatch(optionalMatch.get()));

			onMatchHandlers.forEach(c -> c.accept(option));

		} else {
			onUnmatchedHandlers.forEach(c -> c.accept(option));
		}

		visit((Node) option);
	}

}
