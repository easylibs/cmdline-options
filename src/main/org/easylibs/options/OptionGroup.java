/*
 * 
 */
package org.easylibs.options;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A group of options.
 */
public interface OptionGroup {

	Optional<Option<?>> find(String name);

	<T> Optional<Option<T>> find(String name, Class<T> type);

	Option<?> get(String name) throws OptionNotFoundException;

	<T> Option<T> get(String name, Class<T> type) throws OptionNotFoundException;

	List<Option<?>> getAll(Predicate<Option<?>> predicate);

	List<Option<?>> getAllMatched() throws OptionNotFoundException;

	List<Option<?>> getAllNamed(Predicate<Option<?>> predicate, String... names) throws OptionNotFoundException;

	List<Option<?>> getAllNamed(String... names) throws OptionNotFoundException;

	List<Option<?>> getAllUnmatched();

	<T> Optional<Option<T>> findMatched(String name, Class<T> type);

	Stream<? extends Option<?>> stream();

}
