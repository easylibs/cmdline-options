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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;

/**
 * BeanInfo interface works in conjuction with the {@link Arg} annotation and
 * provides access to a class member for setting its value.
 */
public interface BeanInfo {

	/**
	 * Checks if is final.
	 *
	 * @param member the member
	 * @return true, if is final
	 */
	static boolean isFinal(Member member) {
		return (member.getModifiers() & Modifier.FINAL) == Modifier.FINAL;
	}

	/**
	 * Checks if is static.
	 *
	 * @param member the member
	 * @return true, if is static
	 */
	static boolean isStatic(Member member) {
		return (member.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
	}

	/**
	 * Checks if is writable.
	 *
	 * @param member the member
	 * @return true, if is writable
	 */
	static boolean isWritable(Member member) {
		return (member.getModifiers() & Modifier.FINAL) != Modifier.FINAL;
	}

	/**
	 * Of.
	 *
	 * @param obj   the obj
	 * @param field the field
	 * @return the bean
	 */
	static BeanInfo of(final Object obj, final Field field) {
		Objects.requireNonNull(field, "field");

		assert !isFinal(field) : "bean field is marked final " + field;
		assert !isStatic(field) && (obj != null) : "bean object is null for dynamic field " + field;

		return new BeanInfo() {

			@Override
			public Optional<Arg> getAnnotation() {
				return BeanUtils.findAnnotation(field, Arg.class);
			}

			@Override
			public Optional<Class<?>> getGenericType() {
				if (getType().isArray()) {
					return Optional.of(getType().getComponentType());
				}

				return BeanUtils.getGenericType(field.getGenericType());
			}

			@Override
			public Member getMember() {
				return field;
			}

			@Override
			public String getOptionName() {
				return BeanUtils.getOptionName(field);
			}

			@Override
			public Class<?> getType() {
				return field.getType();
			}

			@Override
			public Object getValue() {
				try {
					field.setAccessible(true);
					return field.get(isStatic() ? null : obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BeanException(field.getName(), e);
				}
			}

			@Override
			public void setValue(Object value) {
				try {
					field.setAccessible(true);
					field.set(isStatic() ? null : obj, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BeanException(field.getName(), e);
				}

			}

			@Override
			public String getName() {
				return field.getName();
			}

			public String toString() {
				return "Bean ["
						+ "field=" + field.getDeclaringClass().getSimpleName() + "::" + field.getName()
//						+ (obj == null ? "" : ", obj=" + obj)
						+ "]";
			}

		};
	}

	/**
	 * Of.
	 *
	 * @param obj    the obj
	 * @param method the method
	 * @return the bean
	 */
	static BeanInfo of(final Object obj, final Method method) {
		Objects.requireNonNull(method, "method");

		assert !isStatic(method) && (obj != null) : "bean object is null for dynamic setter " + method;
		assert method.getParameterCount() == 1 : "parameters for " + method;

		return new BeanInfo() {

			@Override
			public Optional<Arg> getAnnotation() {
				return BeanUtils.findAnnotation(method, Arg.class);
			}

			@Override
			public Optional<Class<?>> getGenericType() {
				if (getType().isArray()) {
					return Optional.of(getType().getComponentType());
				}

				return BeanUtils.getGenericType(method.getGenericParameterTypes()[0]);
			}

			@Override
			public Member getMember() {
				return method;
			}

			@Override
			public String getOptionName() {
				return BeanUtils.getOptionName(method);
			}

			@Override
			public Class<?> getType() {
				return method.getParameterTypes()[0];
			}

			@Override
			public Object getValue() {
				return null;
			}

			@Override
			public void setValue(Object value) {
				try {
					method.setAccessible(true);
					method.invoke(isStatic() ? null : obj, value);
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new BeanException(method.getName(), e);
				}

			}

			public String toString() {
				return "Bean ["
						+ "method=" + method.getDeclaringClass().getSimpleName() + "::" + method.getName()
//						+ (obj == null ? "" : ", obj=" + obj)
						+ "]";
			}

			@Override
			public String getName() {
				return method.getName();
			}
		};
	}

	/**
	 * Gets the annotation.
	 *
	 * @return the annotation
	 */
	Optional<Arg> getAnnotation();

	/**
	 * Gets the generic type.
	 *
	 * @return the generic type
	 */
	Optional<Class<?>> getGenericType();

	/**
	 * Gets the member.
	 *
	 * @return the member
	 */
	Member getMember();

	/**
	 * Gets the option name.
	 *
	 * @return the option name
	 */
	String getOptionName();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	Class<?> getType();

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	Object getValue();

	/**
	 * Checks if is dynamic.
	 *
	 * @return true, if is dynamic
	 */
	default boolean isDynamic() {
		return !isStatic(getMember());
	}

	/**
	 * Checks if is final.
	 *
	 * @return true, if is final
	 */
	default boolean isFinal() {
		return isFinal(getMember());
	}

	/**
	 * Checks if is optional.
	 *
	 * @return true, if is optional
	 */
	default boolean isOptional() {
		return getAnnotation().map(Arg::optional).orElse(false);
	}

	/**
	 * Checks if is static.
	 *
	 * @return true, if is static
	 */
	default boolean isStatic() {
		return isStatic(getMember());
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	void setValue(Object value);
}
