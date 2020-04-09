/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
