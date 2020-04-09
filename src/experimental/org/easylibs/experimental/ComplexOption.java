package org.easylibs.experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easylibs.options.TypeMapper;
import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.graph.OptionNode;

public interface ComplexOption<T> extends Choice, HasOptionName {

	static <T> ComplexOption<T> empty() {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<String> fromDefinition(String definition) {
		throw new UnsupportedOperationException();
	}

	static <T> ComplexOption<T> of(String name, Class<T> type) {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<?> of(String name, Class<?>... parameterTypes) {
		throw new UnsupportedOperationException();
	}

	static <T, C extends List<?>> ComplexOption<List<T>> ofList(
			String name,
			Class<T> parameterType) {
		return new OptionNode<>(name, ArrayList::new);
	}

	static <T> ComplexOption<T> of(String name, TypeMapper<T> mapper) {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<?> of(String name, TypeMapper<?>... mapper) {
		throw new UnsupportedOperationException();
	}

	<U> void setType(Class<U> type, TypeMapper<U> typeMapper);

	<U> void setType(Class<U> type);

	void min(int min);

	void max(int max);

	int min();

	int max();

	boolean isCompound();

	Optional<T> value();

	Optional<String> valueName();

	Optional<Dialect> dialect();
}
