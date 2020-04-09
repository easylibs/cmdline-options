package org.easylibs.options.graph;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.easylibs.extoptions.CompoundOption;
import org.easylibs.extoptions.ComplexOption;
import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.dialect.Match;
import org.easylibs.options.util.TypeMapper;
import org.easylibs.options.util.OptionsRegistry;

public class OptionNode<T> extends AbstractChoice implements ComplexOption<T> {

	private Optional<T> value = Optional.empty();

	String definition;
	Optional<String> valueName = Optional.empty();
	Dialect optionalDialect;
	Optional<CompoundOption> compound = Optional.empty();

	private int min = 0;

	private int max = 0;

	private Match match;

	@SuppressWarnings("unchecked")
	private TypeMapper<T> typeMapper = a -> (T) a;

	OptionsRegistry registry;

	public OptionNode(final String definition) {
		super(null);

		this.definition = definition;
	}

	public OptionNode(String name, String definition) {
		super(name);
		this.definition = definition;
	}

	public OptionNode(String name, Supplier<List<T>> supplier) {
		super(name);
	}

	public Optional<CompoundOption> compound() {
		return this.compound;
	}

	@Override
	public Optional<Dialect> dialect() {
		return Optional.ofNullable(optionalDialect);
	}

	@Override
	public boolean isCompound() {
		return compound.isPresent();
	}

	@Override
	public boolean isPresent() {
		return (match != null);
	}

	@Override
	public int max() {
		return max;
	}

	@Override
	public void max(int max) {
		this.max = max;

		if (max == 0) {
			setType(boolean.class);
		}
	}

	@Override
	public int min() {
		return min;
	}

	@Override
	public void min(int min) {
		this.min = min;
	}

	public void setMatch(Match match) {
		this.match = match;

		try {

			if (max == 0) {
				this.value = Optional.of(typeMapper.mapFrom("true"));

			} else if ((min == 1) && (max == 1)) {
				this.value = Optional.of(typeMapper.mapFrom(this.match.matchedArgs().get(1)));
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> void setType(Class<U> type, TypeMapper<U> typeMapper) {
		this.typeMapper = (TypeMapper<T>) typeMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> void setType(Class<U> type) {
		if (registry == null) {
			throw new IllegalStateException("option not attached to a graph, registry not found");
		}

		this.typeMapper = (TypeMapper<T>) registry.get(type);
	}

	@Override
	public String toString() {
		return "Option ["
				+ "name='" + name() + "'"
//				+ (valueName.isPresent() ? ", valueName='" + valueName.get() + "'" : "")
				+ (value.isPresent() ? ", value='" + value.get() + "'" : "")
//				+ (definition != null ? ", def='" + definition + "'" : "")
//				+ (min > 0 ? ", min=" + min : "")
//				+ (max != 0 ? ", max=" + (max == Integer.MAX_VALUE ? "unlimited" : max) : "")
//				+ (match != null ? ", match='" + match + "'" : "")
//				+ (compound.isPresent() ? ", " + compound.get() : "")
				+ "]";
	}

	@Override
	public Optional<T> value() {
		return value;
	}

	public Optional<String> valueName() {
		return valueName;
	}

	@Override
	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

}