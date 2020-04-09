package org.easylibs.options.graph;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.easylibs.experimental.Choice;
import org.easylibs.experimental.ComplexOption;
import org.easylibs.experimental.Group;
import org.easylibs.experimental.NoSuchOptionException;
import org.easylibs.options.TypeRegistry;

public interface Graph extends Choice {

	public static final String ROOT_NAME = "main";

	static Graph fromSpec(InputStream stream) {
		throw new UnsupportedOperationException();
	}

	static Graph of() {
		return new GraphNode();
	}

	Group add(Group parent);

	Group findGroup(String name) throws NoSuchOptionException;

	<T> Optional<ComplexOption<T>> findOption(String name, Class<T> type);

	Optional<ComplexOption<String>> findOption(String name);

	default void forEach(Consumer<ComplexOption<?>> action) {
		stream().forEach(action);
	}

	Graph group(String name, Consumer<Group> consumer);

	boolean isRoot(Group group);

	ComplexOption<String> getOption(String name) throws NoSuchOptionException;

	<T> ComplexOption<T> getOption(String name, Class<T> type) throws NoSuchOptionException;

	default Stream<ComplexOption<?>> stream() {
		return GraphStream.options((GraphNode) this);
	}

	TypeRegistry registry();
}
