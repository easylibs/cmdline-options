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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericType.
 */
final class GenericType {

	/** The cache. */
	private static Map<String, GenericType> cache = new HashMap<>();

	/**
	 * Of.
	 *
	 * @param type the type
	 * @return the generic type
	 */
	static GenericType of(Class<?> type) {
		return of(type, Optional.empty());
	}

	/**
	 * Of.
	 *
	 * @param type        the type
	 * @param genericType the generic type
	 * @return the generic type
	 */
	static GenericType of(Class<?> type, Optional<Class<?>> genericType) {
		final String key = type.getCanonicalName() + "+" + genericType
				.map(Class::getCanonicalName)
				.orElse("");

		if (cache.containsKey(key)) {
			return cache.get(key);
		}

		GenericType value = new GenericType(type, genericType);
		cache.put(key, value);

		return value;
	}

	/** The type. */
	private final Class<?> type;
	
	/** The generic type. */
	private final Class<?> genericType;

	/**
	 * Instantiates a new generic type.
	 *
	 * @param type        the type
	 * @param genericType the generic type
	 */
	private GenericType(Class<?> type, Optional<Class<?>> genericType) {
		this.type = type;
		this.genericType = genericType.orElse(null);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof GenericType) {
			final GenericType gt = (GenericType) obj;

			return (gt.type == this.type) && (gt.genericType == this.genericType);
		}

		if (obj instanceof String) {
			return ((String) obj).equalsIgnoreCase(type.getSimpleName());
		}

		return false;
	}

	/**
	 * Gets the generic type.
	 *
	 * @return the generic type
	 */
	public Class<?> getGenericType() {
		return genericType;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(type, genericType);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "GenericType ["
				+ "type=" + type.getSimpleName()
				+ ", genericType=" + ((genericType == null) ? "" : genericType.getSimpleName())
				+ "]";
	}

}