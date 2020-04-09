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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Auto-generated Javadoc
/**
 * The Class BeanUtils.
 */
final class BeanUtils {

	/**
	 * Gets the generic type.
	 *
	 * @param type the type
	 * @return the generic type
	 */
	public static Optional<Class<?>> getGenericType(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) type;
			Class<?> genericType = (Class<?>) aType.getActualTypeArguments()[0];

			return Optional.of(genericType);
		}

		return Optional.empty();
	}

	/**
	 * Instantiates a new bean utils.
	 */
	private BeanUtils() {
	}

	/**
	 * Find annotation.
	 *
	 * @param <A>    the generic type
	 * @param cl     the cl
	 * @param aClass the a class
	 * @return the optional
	 */
	public static <A extends Annotation> Optional<A> findAnnotation(Class<?> cl, Class<A> aClass) {
		if (cl.isAnnotationPresent(Arg.class)) {
			return Optional.of(cl.getAnnotation(aClass));
		}

		return cl.getSuperclass() == null ? Optional.empty() : findAnnotation(cl.getSuperclass(), aClass);
	}

	/**
	 * Find annotation.
	 *
	 * @param <M>    the generic type
	 * @param <A>    the generic type
	 * @param member the member
	 * @param aClass the a class
	 * @return the optional
	 */
	public static <M extends Member & AnnotatedElement, A extends Annotation> Optional<A> findAnnotation(M member,
			Class<A> aClass) {

		if (member.isAnnotationPresent(aClass)) {
			return Optional.of(member.getAnnotation(aClass));
		}

		return findAnnotation(member.getDeclaringClass(), aClass);
	}

	/**
	 * Checks if is annotation.
	 *
	 * @param <M>    the generic type
	 * @param <A>    the generic type
	 * @param member the member
	 * @param aClass the a class
	 * @return true, if is annotation
	 */
	public static <M extends Member & AnnotatedElement, A extends Annotation> boolean isAnnotation(M member,
			Class<A> aClass) {

		return findAnnotation(member, Arg.class).isPresent();
	}

	/**
	 * Gets the option name.
	 *
	 * @param <M>    the generic type
	 * @param member the member
	 * @return the option name
	 */
	public static <M extends Member & AnnotatedElement> String getOptionName(M member) {
		final Optional<Arg> arg = Optional.ofNullable(member.getAnnotation(Arg.class));

//		arg.ifPresent(System.out::println);

		return arg.filter(a -> !a.name().isEmpty())
				.map(Arg::name)
				.orElse(dashSplitter(member.getName()));

	}

	/** The dashsplitter pattern. */
	private static Pattern DASHSPLITTER_PATTERN = Pattern.compile(""
			+ "^set" // Remove 'set' prefix from setAbc
			+ "|\\p{Upper}{2,}[\\d\\p{Punct}]*" // Treat multiple uppers as break up word
			+ "|\\p{Upper}{1}[\\p{Lower}\\d]+\\p{Punct}*" // Breakup each Capitilized word
			+ "|[\\p{Lower}\\d\\p{Punct}]+" // if entire or ending all lowercase word
			+ "|\\p{Upper}$" // If last letter is capitalized breakit up
			+ "");

	/**
	 * Dash splitter.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String dashSplitter(String str) {
		final StringBuilder sb = new StringBuilder();

		final Matcher m = DASHSPLITTER_PATTERN.matcher(str);
		while (m.find()) {
			final String match = m.group();

			if ("set".equals(match)) {
				continue;
			}

			if (sb.length() > 0) {
				sb.append('-');
			}

			sb.append(match.toLowerCase());

		}

//		System.out.println(str + "=" + sb.toString());

		return sb.toString();
	}

	/**
	 * Bean to setter.
	 *
	 * @param beanName the bean name
	 * @return the string
	 */
	public static String beanToSetter(String beanName) {
		if (beanName.isEmpty()) {
			return beanName;
		}

		final String first = beanName.substring(0, 1).toUpperCase();

		return (beanName.length() == 1)
				? "set" + first
				: "set" + first + beanName.substring(1);
	}

	/**
	 * To string.
	 *
	 * @param bean the bean
	 * @return the string
	 */
	public static String toString(BeanInfo bean) {
		final String name = bean.getName();
		Object value = bean.getValue();

		/* deep decode arrays */
		if (value != null && value.getClass().isArray()) {
			String str = Arrays.deepToString(new Object[] { value });
			str = str.substring(1, str.length() - 1); // drop the outter []

			value = str;
		}

		return name + "=" + String.valueOf(value);
	}

	/**
	 * To string.
	 *
	 * @param bean the bean
	 * @return the string
	 */
	public static String toString(Object bean) {
		return toStream(bean)
				.map(BeanUtils::toString)
				.collect(Collectors.joining(", "));
	}

	/**
	 * To stream.
	 *
	 * @param bean the bean
	 * @return the stream
	 */
	public static Stream<BeanInfo> toStream(Object bean) {
		final Class<?> cl = (bean instanceof Class) ? (Class<?>) bean : bean.getClass();
		final Object obj = (bean instanceof Class) ? null : bean;

		return Stream.of(cl.getDeclaredFields())
				.map(f -> BeanInfo.of(obj, f));
	}

	/**
	 * Find bean.
	 *
	 * @param objOrClass the obj or class
	 * @param beanName   the bean name
	 * @return the optional
	 */
	public static Optional<BeanInfo> findBean(Object objOrClass, String beanName) {

		final Object obj = (objOrClass instanceof Class)
				? null
				: objOrClass;

		final Class<?> cl = (objOrClass instanceof Class)
				? (Class<?>) objOrClass
				: objOrClass.getClass();

		final String setterName = beanToSetter(beanName);

		final Optional<BeanInfo> setter = Stream.of(cl.getDeclaredMethods())
				.filter(m -> isAnnotation(m, Arg.class))
				.filter(m -> !findAnnotation(m, Arg.class).get().ommit())
				.filter(m -> m.getName().equals(setterName))
				.filter(m -> (m.getParameterCount() == 1))
				.filter(m -> (obj != null) || (obj == null) && BeanInfo.isStatic(m))
				.map(m -> BeanInfo.of(obj, m))
				.findFirst();

		if (setter.isPresent()) {
			return setter;
		}

		try {

			return Optional.of(cl.getDeclaredField(beanName))
					.filter(f -> isAnnotation(f, Arg.class))
					.filter(f -> !findAnnotation(f, Arg.class).get().ommit())
					.map(f -> BeanInfo.of(obj, f));

		} catch (NoSuchFieldException | SecurityException e) {
			return Optional.empty();
		}
	}

	/**
	 * Find all beans in the container supplied container.
	 *
	 * @param supplier the supplier which returns a container
	 * @return the list of beans found
	 */
	public static List<BeanInfo> findAllBeans(Supplier<Object> supplier) {
		return findAllBeans(supplier.get());
	}

	/**
	 * Finds all beans in the supplied opaque container. This method will
	 * recursively evaluate the container and look for beans. The container can be a
	 * class, a user object or an array or a collection of sub-containers, which
	 * will recursively be scanned for beans.
	 * <h1>Bean requirements</h2>
	 * <p>
	 * The bean can be a field or a setter method and must be annoated with an
	 * {@link Arg} annotation. Field beans must not have the {@link Modifier#FINAL}
	 * defined and setter methods must take a single parameter of any of the
	 * supported types by the {@link TypeRegistry}.
	 * </p>
	 * <p>
	 * Further more, when dynamic user object is supplied as a container, both
	 * static and dynamic beans (fields and setter methods) are selected. When a
	 * {@link Class} object is supplied, only static beans are selected.
	 * </p>
	 * <p>
	 * For setter method beans, the method's return value is ignored, only the first
	 * parameter type is relavent, as it determines the option value type. A
	 * {@link TypeRegistry} lookup for the type will be made for an
	 * appropriate {@link TypeMapper}.
	 * </p>
	 * 
	 * <h2>Example 1</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(MyClass.class)
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 *
	 * <h2>Example 2</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(new MyClass())
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 *
	 * <h2>Example 3</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(new MyClass(), new MyClass2())
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 * 
	 * <h2>Example 4</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(new MyClass(), MyClass2.class)
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 *
	 * <h2>Example 5</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(new Object[] {new MyClass(), MyClass2.class})
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 *
	 * <h2>Example 6</h2>
	 * 
	 * <pre>
	 * <code>
	 * BeanUtils.findAllBeans(Arrays.asList(new MyClass(), MyClass2.class))
	 * 	.forEach(System.out::println);
	 * </code>
	 * </pre>
	 *
	 * @param container the supplied container can be any of the following
	 *                  <ul>
	 *                  <li>any dynamic object whose class will be scanned for
	 *                  beans</li>
	 *                  <li>a user defined class which will be scanned for
	 *                  beans</li>
	 *                  <li>an array of sub-containers which will be scanned
	 *                  recursively for beans</li>
	 *                  <li>a collection of sub-containers which will be scanned
	 *                  recursively for beans</li>
	 *                  <li>A {@link Supplier} of a container which will be used to
	 *                  get a container which will then be recursively scanned for
	 *                  beans</li>
	 *                  </ul>
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static List<BeanInfo> findAllBeans(Object container) {

		final List<BeanInfo> allBeans = new ArrayList<>();

		/* container == Object[] */
		if (container.getClass().isArray()) {
			Stream.of((Object[]) container)
					.map(BeanUtils::findAllBeans)
					.forEach(allBeans::addAll);

			return allBeans;
		}

		/* container == Collection<Object> */
		if (container instanceof Collection) {
			((Collection<Object>) container).stream()
					.map(BeanUtils::findAllBeans)
					.forEach(allBeans::addAll);

			return allBeans;
		}

		/* container == Supplier<Object> */
		if (container instanceof Supplier) {
			return findAllBeans(((Supplier<Object>) container).get());
		}

		/* container == Class<?> || container == Object */
		final Object obj = (container instanceof Class)
				? null
				: container;

		final Class<?> cl = (container instanceof Class)
				? (Class<?>) container
				: container.getClass();

		Stream.of(cl.getDeclaredMethods())
				.filter(m -> isAnnotation(m, Arg.class))
				.filter(m -> !findAnnotation(m, Arg.class).get().ommit())
				.filter(m -> (m.getParameterCount() == 1))
				.filter(m -> (obj != null) || (obj == null) && BeanInfo.isStatic(m))
				.map(m -> BeanInfo.of(obj, m))
//				.peek(b -> System.out.println("Adding: " + b))
				.forEach(allBeans::add);

		Stream.of(cl.getDeclaredFields())
				.filter(BeanInfo::isWritable)
				.filter(f -> isAnnotation(f, Arg.class))
				.filter(f -> !findAnnotation(f, Arg.class).get().ommit())
				.filter(f -> (obj != null) || (obj == null) && BeanInfo.isStatic(f))
				.map(f -> BeanInfo.of(obj, f))
//				.peek(b -> System.out.println("Adding: " + b))
				.forEach(allBeans::add);

		return allBeans;
	}

}
