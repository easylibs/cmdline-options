/*
 * 
 */
package org.easylibs.options;

import java.util.Optional;

// TODO: Auto-generated Javadoc
/**
 * The Interface IsMutableOption.
 *
 * @param <T> the generic type
 */
interface IsMutableOption<T> extends Option<T> {

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	Class<T> getType();

	/**
	 * Gets the generic type.
	 *
	 * @return the generic type
	 */
	default Optional<Class<?>> getGenericType() {
		return Optional.empty();
	}

	/**
	 * Sets the mapper.
	 *
	 * @param mapper the new mapper
	 */
	void setMapper(TypeMapper<T> mapper);

	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	TypeMapper<T> getMapper();

	/**
	 * Sets the accumulator.
	 *
	 * @param setter the new accumulator
	 */
	void setAccumulator(Accumulator<T> setter);

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	void setValue(T value);

	/**
	 * Min.
	 *
	 * @return the int
	 */
	int min();

	/**
	 * Max.
	 *
	 * @return the int
	 */
	int max();

	/**
	 * Checks if is optional.
	 *
	 * @return true, if is optional
	 */
	boolean isOptional();

	/**
	 * Sets the match.
	 *
	 * @param b the new match
	 */
	void setMatch(boolean b);

}
