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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleOption.
 *
 * @param <T> the generic type
 */
class SimpleOption<T> implements Option<T>, IsMutableOption<T> {

	/** The name. */
	private final String name;
	
	/** The option value. */
	private T optionValue;
	
	/** The type. */
	private final Class<T> type;
	
	/** The optional. */
	private final boolean optional;
	
	/** The mapper. */
	private TypeMapper<T> mapper;
	
	/** The min. */
	private int min;
	
	/** The max. */
	private int max;
	
	/** The on match. */
	private Optional<Consumer<T>> onMatch = Optional.empty();

	/** The accumulator. */
	protected Accumulator<T> accumulator = Accumulator.identity();
	
	/** The match count. */
	private int matchCount;

	/**
	 * Instantiates a new simple option.
	 *
	 * @param name     the name
	 * @param type     the type
	 * @param optional the optional
	 */
	public SimpleOption(String name, Class<T> type, boolean optional) {
		this.name = name;
		this.type = type;
		this.optional = optional;

		final boolean bool = type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class);
		if (!bool) {
			min = optional ? 0 : 1;
			max = 1;
		}
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Option) {
			return name.equals(((Option<?>) obj).getName());
		}

		return false;
	}

	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	@Override
	public TypeMapper<T> getMapper() {
		return mapper;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Class<T> getType() {
		return this.type;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Checks if is optional.
	 *
	 * @return true, if is optional
	 */
	@Override
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Checks if is matched.
	 *
	 * @return true, if is matched
	 */
	@Override
	public boolean isMatched() {
		return matchCount > 0;
	}

	/**
	 * Max.
	 *
	 * @return the int
	 */
	@Override
	public int max() {
		return max;
	}

	/**
	 * Min.
	 *
	 * @return the int
	 */
	@Override
	public int min() {
		return min;
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * On match.
	 *
	 * @param handler the handler
	 * @return the option
	 */
	@Override
	public Option<T> onMatch(Consumer<T> handler) {
		this.onMatch = Optional.of(handler);
		return this;
	}

	/**
	 * Sets the match.
	 *
	 * @param b the new match
	 */
	@Override
	public void setMatch(boolean b) {
		this.matchCount += (b ? 1 : -1);
		onMatch.ifPresent(h -> h.accept(this.optionValue));
	}

	/**
	 * Sets the mapper.
	 *
	 * @param mapper the new mapper
	 */
	@Override
	public void setMapper(TypeMapper<T> mapper) {
		Objects.requireNonNull(mapper);
		this.mapper = mapper;
	}

	/**
	 * Sets the accumulator.
	 *
	 * @param accumulator the new accumulator
	 */
	@Override
	public void setAccumulator(Accumulator<T> accumulator) {
		Objects.requireNonNull(accumulator);

		this.accumulator = accumulator;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(T value) {
		store(accumulator.accumulate(optionValue, value));
	}

	/**
	 * Store.
	 *
	 * @param value the value
	 */
	protected void store(T value) {
		this.optionValue = value;
		setMatch(true);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Option ["
				+ "name='" + name + "'"
				+ (optionValue != null ? ", value='" + optionValue + "'" : "")
				+ (optional ? ", arg-optional" : "")
				+ (matchCount > 0 ? ", matches=" + matchCount : "")
				+ (min == 1 && max == 1 ? ", arg-required" : "")
				+ toStringAdditions()
				+ ", type=" + type.getSimpleName()
				+ "]";
	}

	/**
	 * To string additions.
	 *
	 * @return the string
	 */
	protected String toStringAdditions() {
		return "";
	}

	/**
	 * Value.
	 *
	 * @return the t
	 */
	@Override
	public T getValue() {
		return optionValue;
	}
}
