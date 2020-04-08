/*
 * 
 */
package org.easylibs.options;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * <p>
 * Accumultator interface is used to accumulate command line arguments as
 * composite option value. The interface allows custom processing to be done
 * when a new value is to be assigned to an option. For example, when the value
 * type is a collection, instead of overriding the option value with a new
 * collection with a single value, the accumulator can check if a collection
 * container already exists and simply append the new value to the existing
 * collection. Similarly with arrays, accumulators are used to reallocated
 * arrays so that a new value can fit, etc.
 * </p>
 * 
 * <p>
 * Accumulator interface is used with the {@link TypeRegistry} and
 * {@link TypeMapper} interfaces. Default accumulators are registered for array
 * and collection types, when automatic mapping is performed. However, any
 * mapping can be overriden including different or no accumulators for each
 * type.
 * </p>
 *
 * @param <T> the generic type the accumulator will be used on
 */
public interface Accumulator<T> {

	/**
	 * Identity.
	 *
	 * @param <T> the generic type
	 * @return the accumulator
	 */
	static <T> Accumulator<T> identity() {
		return (a, b) -> b;
	}

	/**
	 * Default accumulator for all collection types.
	 *
	 * @param <T> the generic type
	 * @return the accumulator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static <T extends Collection<?>> Accumulator<T> collections() {

		return (a, b) -> {
			if (a == null) {
				return b;
			}

			a.addAll((Collection) b);

			return a;
		};
	}

	/**
	 * Default accumulator for all array types
	 *
	 * @param <T> the generic type
	 * @return the accumulator
	 */
	@SuppressWarnings("unchecked")
	static <T> Accumulator<T> array() {
		return (a, b) -> {
			if (a == null) {
				return b;
			}

			final int a1 = Array.getLength(a);
			final int b1 = Array.getLength(b);

			final Object c = Array.newInstance(b.getClass().getComponentType(), a1 + b1);

			System.arraycopy(a, 0, c, 0, a1);
			System.arraycopy(b, 0, c, a1, b1);

			return (T) c;
		};
	}

	/**
	 * Perform the accumulate computation
	 *
	 * @param oldValue previous accumulator value
	 * @param newValue new accumulator value
	 * @return the t
	 */
	T accumulate(T oldValue, T newValue);

	/**
	 * Accumulate on a new value only
	 *
	 * @param newValue the new value
	 * @return the t
	 */
	default T accumulate(T newValue) {
		return accumulate(null, newValue);
	}
}
