package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.easylibs.experimental.CompoundOption;
import org.easylibs.options.TypeMapper;
import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.dialect.Match;

public class CompoundNode extends LeafNode implements CompoundOption {

	private static Optional<String> parseValueName(String definition) {
		definition = definition
				.replaceFirst("[\\.]{3}$", "")
				.replaceFirst("\\[.*\\]$", "")

		;

		return Optional.ofNullable(definition.matches("-[\\w\\d]{1}[\\s]*[\\S]+")
				? definition.substring(2)
				: null);
	}

	private static String parseOptionName(String definition) {
		return definition.matches("^-[\\w]?.*") ? definition.substring(1, 2) : null;
	}

	private String definition;
	private Optional<String> valueName;
	private final List<Match> matches = new ArrayList<>();

	@Override
	public List<Match> matches() {
		return matches;
	}

	private int min;

	private int max = Integer.MAX_VALUE;

	public CompoundNode(String definition) {
		super(parseOptionName(definition));
		this.definition = definition;

		this.valueName = parseValueName(definition);
	}

	public CompoundNode(String name, String definition) {
		super(name);

		this.definition = definition;
		this.valueName = parseValueName(definition);
	}

	@Override
	public String toString() {
		return "CompoundOption ["
				+ "name='" + name() + "'"
//				+ (valueName.isPresent() ? ", valueName='" + valueName.get() + "'" : "")
				+ (value().isPresent() ? ", value='" + value().get() + "'" : "")
//				+ ", definition='" + definition + "'"
//				+ ", matches=" + matches.stream().map(m -> m.option().name()).collect(Collectors.toList())
				+ "]";
	}

	@Override
	public void addMatch(Match match) {
		this.matches.add(match);
	}

	@Override
	public <U> void setType(Class<U> type, TypeMapper<U> typeMapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <U> void setType(Class<U> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void min(int min) {
		this.min = min;
	}

	@Override
	public void max(int max) {
		this.max = max;
	}

	@Override
	public int min() {
		return min;
	}

	@Override
	public int max() {
		return max;
	}

	@Override
	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean isCompound() {
		return true;
	}

	@Override
	public Optional<List<String>> value() {
		return matches.isEmpty()
				? Optional.empty()
				: Optional.of(
						matches.stream()
								.map(m -> m.option().name() + "=" + m.option().value().get())
								.collect(Collectors.toList()));
	}

	@Override
	public Optional<String> valueName() {
		return valueName;
	}

	@Override
	public Optional<Dialect> dialect() {
		return findDown(Dialect.class);
	}

	@Override
	public boolean isPresent() {
		return !matches.isEmpty();
	}

}
