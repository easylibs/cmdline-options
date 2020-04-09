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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A registry for {@link TypeMapper}. {@link TypeMapper} mappings are used to
 * parse and map command line arguments to java types. Since all command line
 * arguments are strings, the mapper takes a java {@code String} as input and
 * performs the neccessary convertion to the mapper type. The mapper will throw
 * checked and unchecked exceptions on any conversion errors.
 * 
 * @see #register
 */
public class TypeRegistry {

	/**
	 * The Class TypeMapperImpl.
	 *
	 * @param <T> the generic type
	 */
	private static class TypeMapperImpl<T> implements TypeMapper<T> {

		/** The priority. */
		private final int priority;

		/** The mapper. */
		private final TypeMapper<T> mapper;

		/** The accumulator. */
		private final Accumulator<T> accumulator;

		/**
		 * Instantiates a new type mapper impl.
		 *
		 * @param priority    the priority
		 * @param mapper      the mapper
		 * @param accumulator the accumulator
		 */
		TypeMapperImpl(
				final int priority,
				final TypeMapper<T> mapper,
				final Accumulator<T> accumulator) {
			this.priority = priority;
			this.mapper = mapper;
			this.accumulator = accumulator;
		}

		/**
		 * Instantiates a new type mapper impl.
		 *
		 * @param mapper      the mapper
		 * @param accumulator the accumulator
		 */
		TypeMapperImpl(
				final TypeMapper<T> mapper,
				final Accumulator<T> accumulator) {
			this(0, mapper, accumulator);
		}

		/**
		 * Accumulator.
		 *
		 * @return the accumulator
		 */
		@Override
		public Accumulator<T> accumulator() {
			return accumulator;
		}

		/**
		 * Map from.
		 *
		 * @param value the value
		 * @return the t
		 * @throws Throwable the throwable
		 */
		@Override
		public T mapFrom(String value) throws Throwable {
			return mapper.mapFrom(value);
		}

		/**
		 * Priority.
		 *
		 * @return the int
		 */
		@Override
		public int priority() {
			return priority;
		}

	}

	/** The Constant GLOBAL_REGISTRY. */
	private static final TypeRegistry GLOBAL_REGISTRY;

	static {
		GLOBAL_REGISTRY = new TypeRegistry();
		GLOBAL_REGISTRY.registerDefaults();
	}

	public static <T> Registration register(Class<T> type, TypeMapper<T> mapper) {
		return global().set(type, mapper);
	}

	public static <T> Registration register(Class<T> type, TypeMapper<T> mapper, T defaultValue) {
		return global().set(type, mapper, defaultValue);
	}

	public static <T> Registration register(Class<T> type, TypeMapper<T> mapper, Accumulator<T> accumulator) {
		return global().set(type, mapper, accumulator);
	}

	/**
	 * Global.
	 *
	 * @return the options registry
	 */
	public static TypeRegistry global() {
		return GLOBAL_REGISTRY;
	}

	/**
	 * Parses the array.
	 *
	 * @param <T>           the generic type
	 * @param componentType the component type
	 * @param str           the str
	 * @param mapper        the mapper
	 * @return the t
	 * @throws Throwable the throwable
	 */
	@SuppressWarnings("unchecked")
	private static <T> T parseArray(Class<?> componentType, final String str, TypeMapper<?> mapper)
			throws Throwable {
		final String[] split = str.split(",");

		final Object array = Array.newInstance(componentType, split.length);

		for (int i = 0; i < split.length; i++) {
			String string = split[i];

			Object value = mapper.mapFrom(string);
			Array.set(array, i, value);
		}

		return (T) array;
	}

	/**
	 * Parses the boolean.
	 *
	 * @param str the str
	 * @return the boolean
	 */
	private static Boolean parseBoolean(final String str) {
		switch (str.toLowerCase()) {
		case "true":
		case "1":
		case "on":
			return true;

		case "false":
		case "0":
		case "off":
			return false;

		default:
			try {
				return Long.parseLong(str) != 0;
			} catch (final NumberFormatException e) {
			}

			throw new NumberFormatException("invalid boolean value " + str);
		}
	}

	/**
	 * Parses the list.
	 *
	 * @param <U>    the generic type
	 * @param str    the str
	 * @param mapper the mapper
	 * @return the list
	 * @throws Throwable the throwable
	 */
	private static <U> List<U> parseList(final String str, TypeMapper<U> mapper) throws Throwable {

		ArrayList<U> col = new ArrayList<>();
		String[] array = str.split(",");

		for (String a : array) {
			col.add(mapper.mapFrom(a));
		}

		return col;
	}

	/**
	 * Parses the set.
	 *
	 * @param <U>    the generic type
	 * @param str    the str
	 * @param mapper the mapper
	 * @return the sets the
	 * @throws Throwable the throwable
	 */
	private static <U> Set<U> parseSet(final String str, TypeMapper<U> mapper) throws Throwable {
		return new HashSet<>(parseList(str, mapper));
	}

	/** The registry. */
	private final Map<GenericType, TypeMapper<?>> registry = new HashMap<>();

	/**
	 * Instantiates a new options registry.
	 */
	private TypeRegistry() {
	}

	/**
	 * Gets the.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the type mapper
	 */
	@SuppressWarnings("unchecked")
	public <T> TypeMapper<T> get(final Class<T> type) {
		return (TypeMapper<T>) registry.get(GenericType.of(type));
	}

	/**
	 * Gets the.
	 *
	 * @param <T>         the generic type
	 * @param type        the type
	 * @param genericType the generic type
	 * @return the type mapper
	 */
	@SuppressWarnings("unchecked")
	public <T> TypeMapper<T> get(final Class<T> type, Optional<Class<?>> genericType) {
		return (TypeMapper<T>) registry.get(GenericType.of(type, genericType));
	}

	/**
	 * <p>
	 * If no mapper is defined for a type, this method will try one of its many
	 * algorithms to come up with a mapper that will work with the requested type.
	 * For example, all collection, array and enum types are automatically created,
	 * if no user defined custom mapping exists. Mappings are done recursively.
	 * </p>
	 * 
	 * <p>
	 * For example if we have a {@code Collection<Integer>}, then one mapping for
	 * {@code Collection} is created is created and another mapping for
	 * {@code Integer}. The mapping is cached as a concrete-generic pair. So a
	 * mapping of {@code Collection<Float>} will be different from
	 * {@code Collection<Integer>}.
	 * </p>
	 *
	 * @param <T>         the generic type
	 * @param type        the type
	 * @param genericType if the mapper type is generic, the generic type must be
	 *                    provided
	 * @return the or else auto create
	 */
	public <T> TypeMapper<T> getOrElseAutoCreate(final Class<T> type, Optional<Class<?>> genericType) {

		TypeMapper<T> mapper = get(type, genericType);
		if (mapper == null) {
			mapper = tryAutoCreate(type, genericType);
		}

		return mapper;
	}

	public <T> Registration set(final Class<T> type, final TypeMapper<T> mapper) {
		final GenericType gt = GenericType.of(type);

		registry.put(GenericType.of(type), mapper);

		return () -> registry.remove(gt);
	}

	/**
	 * Register a new mapping
	 *
	 * @param <T>    the generic type
	 * @param type   the type
	 * @param mapper the mapper
	 * @return the registration
	 */
	public <T> Registration set(final Class<T> type, final TypeMapper<T> mapper, T defaultValue) {

		final GenericType gt = GenericType.of(type);
		registry.put(GenericType.of(type), new TypeMapper<T>() {

			@Override
			public T mapFrom(String value) throws Throwable {
				return mapper.mapFrom(value);
			}

			@Override
			public T defaultValue() {
				return defaultValue;
			}

		});

		return () -> registry.remove(gt);
	}

	public Class<?> lookupType(String name) {
		return registry.entrySet()
				.stream()
				.filter(e -> e.getKey().getType().equals(name))
				.map(e -> e.getKey().getType())
				.findAny()
				.orElse(null);
	}

	public Class<?> lookupTypeIgnoreCase(String name) {

		return registry.entrySet()
				.stream()
				.filter(e -> e.getKey().getType().getSimpleName().equalsIgnoreCase(name))
				.map(e -> e.getKey().getType())
				.findAny()
				.orElse(null);
	}

	/**
	 * Register.
	 *
	 * @param <T>         the generic type
	 * @param type        the type
	 * @param mapper      the mapper
	 * @param accumulator the accumulator
	 * @return the registration
	 */
	public <T> Registration set(
			final Class<T> type,
			final TypeMapper<T> mapper,
			final Accumulator<T> accumulator) {

		final GenericType gt = GenericType.of(type);

		registry.put(gt, new TypeMapperImpl<>(mapper, accumulator));

		return () -> registry.remove(gt);
	}

	/**
	 * Register defaults.
	 */
	private void registerDefaults() {

		register(Byte.class, Byte::parseByte);
		register(Short.class, Short::parseShort);
		register(Integer.class, Integer::parseInt);
		register(Long.class, Long::parseLong);
		register(String.class, String::valueOf);
		set(Boolean.class, TypeRegistry::parseBoolean, true);
		register(Integer.class, Integer::parseInt);
		register(Float.class, Float::parseFloat);
		register(Double.class, Double::parseDouble);

		register(byte.class, Byte::parseByte);
		register(short.class, Short::parseShort);
		register(int.class, Integer::parseInt);
		register(long.class, Long::parseLong);
		set(boolean.class, TypeRegistry::parseBoolean, true);
		register(float.class, Float::parseFloat);
		register(double.class, Double::parseDouble);

		register(Class.class, Class::forName);

	}

	/**
	 * Try auto create.
	 *
	 * @param <T>         the generic type
	 * @param type        the type
	 * @param genericType the generic type
	 * @return the type mapper
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> TypeMapper<T> tryAutoCreate(Class<T> type, Optional<Class<?>> genericType) {

		if (type.isEnum()) {

			final TypeMapper mapper = name -> Stream.of((Enum[]) type.getEnumConstants())
					.filter(c -> c.name().equalsIgnoreCase(name))
					.findFirst()
					.orElseGet(() -> Enum.valueOf((Class) type, name));

			registry.put(GenericType.of(type, genericType), mapper);

			return mapper;
		}

		if (type.isArray()) {
			final TypeMapper<?> componentMapper = getOrElseAutoCreate(type.getComponentType(), Optional.empty());
			if (componentMapper == null) {
				return null;
			}

			final TypeMapper<T> mapper = new TypeMapperImpl<T>(
					s -> (T) parseArray(type.getComponentType(), s, componentMapper),
					Accumulator.array());

			registry.put(GenericType.of(type, genericType), mapper);

			return mapper;
		}

		if ((Collection.class.isAssignableFrom(type) || List.class.isAssignableFrom(type))
				&& genericType.isPresent()) {

			final TypeMapper<?> componentMapper = getOrElseAutoCreate(genericType.get(), Optional.empty());

			final TypeMapper<T> mapper = new TypeMapperImpl(
					s -> (T) parseList(s, componentMapper),
					Accumulator.collections());

			registry.put(GenericType.of(type, genericType), mapper);

			return mapper;
		}

		if ((Set.class.isAssignableFrom(type)) && genericType.isPresent()) {

			final TypeMapper<?> componentMapper = getOrElseAutoCreate(genericType.get(), Optional.empty());

			final TypeMapper<T> mapper = new TypeMapperImpl(
					s -> (T) parseSet(s, componentMapper),
					Accumulator.collections());

			registry.put(GenericType.of(type, genericType), mapper);

			return mapper;
		}

		return null;
	}

}
