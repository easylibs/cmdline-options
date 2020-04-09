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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The {@code Arg} annotation marks a class field or method as a
 * {@link BeanOptionImpl}. It's attributes are used to configure the
 * {@link BeanOptionImpl}.
 * <h1>Field requirments</h1>
 * <p>
 * If a class field is annotated with {@link Arg} annotation, the field must not
 * be marked {@code final} and must otherwise be writable using java reflection
 * API. The field type must also be mappable from a string argument to a value
 * and an appropriate {@link TypeMapper} must be registered with the
 * {@link TypeRegistry}.
 * </p>
 * <p>
 * Any field for an option, which takes no-argument, such as {@code -a}, must be
 * of type boolean. When the option is present on the command line, the field's
 * boolean value will be set to true. Other the field's value won't be changed.
 * </p>
 * <p>
 * The field's value is changed when the option is encountered on the command
 * line and argument is present or for a no-argument option, the boolean field
 * is set to true.
 * </p>
 * <p>
 * If the attribute {@link #optional} is set to true, the field's value is
 * unchanged if the option's argument on the command line is not present.
 * </p>
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD, TYPE })
public @interface Arg {

	/**
	 * A new option name which should follow the Posix standard of lowercase '-'
	 * separated set of words.
	 *
	 * @return the option name
	 */
	String name() default "";

	/**
	 * If a {@link BeanOptionImpl} takes an argument, this attribute makes the argument
	 * optional.
	 * 
	 * <p>
	 * If argument is optional and is not present on the command line, the reflected
	 * bean field or method setter will not be changed or invoked. However the
	 * presence of the option itself on the command line can be checked with the
	 * boolean method {@link Option#isMatched}, which will return {@code true}.
	 * </p>
	 * <p>
	 * If argument is required {@code false} which is the default, an error will be
	 * raised if the argument is missing from the command line. The bean's
	 * underlying class field or method will be set or invoked with the argument
	 * mapped value.
	 * </p>
	 *
	 * @return true, if argument value is optional, otherwise it is required.
	 *         Default if {@code false}.
	 */
	boolean optional() default false;

	/**
	 * The class field or setter method is ommitted as an options Useful when an
	 * annotated class implicitely defines options for every class field but you
	 * want to skip a particular one.
	 *
	 * @return true, if to be ignored and ommitted as an option
	 */
	boolean ommit() default false;
}
