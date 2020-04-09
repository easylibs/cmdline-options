package org.easylibs.options.graph;

import java.util.Optional;

import org.easylibs.extoptions.CompoundOption;
import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.util.OptionsRegistry;

public class LinkerVisitor implements GraphVisitor {

	private final GraphNode graph;

	public LinkerVisitor(GraphNode graph) {
		this.graph = graph;
	}

	public void link() {
		graph.visit(this);
	}

	@Override
	public void visit(Node node) {
	}

	@Override
	public void visit(OptionNode<?> option) {

		final Dialect dialect = option
				.findDown(Dialect.class)
				.orElseThrow(IllegalStateException::new);

		final OptionsRegistry registry = option
				.findDown(OptionsRegistry.class)
				.orElseThrow(IllegalStateException::new);

		final String definition = option.definition;
		final String optionName = dialect.optionName(definition);

		option.optionalDialect = dialect;
		option.registry = registry;

		option.setName(optionName);
		option.compound = option.findDown(CompoundOption.class, comp -> optionName.startsWith(comp.name()));

		option.valueName = dialect.valueName(definition);
		option.valueName.ifPresent(n -> option.min(1));
		option.valueName.ifPresent(n -> option.max(1));
		option.valueName
				.flatMap(n -> parseValueTypeFromValueName(registry, dialect, n))
				.ifPresent(option::setType);

	}

	private Optional<Class<?>> parseValueTypeFromValueName(OptionsRegistry registry, Dialect dialect, String name) {

		final String[] c = name.split(":");

		return (c.length == 2)
				? Optional.of(registry.lookupTypeIgnoreCase((c[1])))
				: Optional.empty();
	}

}
