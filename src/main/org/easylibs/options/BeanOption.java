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

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Bean option that gets its name from a reflected class member's name or if any
 * members are annoted with {@link Arg} annotation, the {@link Arg#name()}
 * attribute of the annotation. Class member names are used as option names,
 * when the annotation doesn't provide an explicit name, using the
 * {@link Arg#name()} attribute. The class member name is converted to a dash
 * separated form from the standard java naming convention of capitalized words
 * concatenated together.
 * </p>
 * <p>
 * The bean option value modifies the class member value when it is set. Each
 * bean option maintains a {@link TypeMapper} instance which allows it to
 * translate the command line string argument to the correct type for the class
 * member. The mappings are maintained in a registry and new types can be
 * registered during the creation of the {@link Options} object using its
 * builder.
 * </p>
 * <p>
 * Full member type and generic/component type mappings are maintained in the
 * registry for collections and arrays as class member types.
 * </p>
 */
public interface BeanOption extends Option<Object> {

	/**
	 * Of.
	 *
	 * @param obj  the obj
	 * @param name the name
	 * @return the option
	 */
	static Option<?> of(Object obj, String name) {
		return BeanUtils.findBean(obj, name)
				.map(BeanOptionImpl::new)
				.orElseThrow(BeanException::new);
	}

	/**
	 * Of.
	 *
	 * @param cl   the cl
	 * @param name the name
	 * @return the option
	 */
	static Option<?> of(Class<?> cl, String name) {
		return BeanUtils.findBean(cl, name)
				.map(BeanOptionImpl::new)
				.orElseThrow(BeanException::new);
	}

	/**
	 * List options.
	 *
	 * @param objs the objs
	 * @return the list
	 */
	static List<Option<?>> listOptions(Object... objs) {
		return BeanUtils.findAllBeans(objs)
				.stream()
				.map(BeanOptionImpl::new)
				.collect(Collectors.toList());
	}

	/**
	 * List options.
	 *
	 * @param classes the classes
	 * @return the list
	 */
	static List<Option<?>> listOptions(Class<?>... classes) {
		return BeanUtils.findAllBeans(classes)
				.stream()
				.map(BeanOptionImpl::new)
				.collect(Collectors.toList());
	}

	BeanInfo getBeanInfo();
}
