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
 * The Class BeanOption.
 */
class BeanOptionImpl extends SimpleOption<Object> implements BeanOption {

	/**
	 * Gets the generic type.
	 *
	 * @return the generic type
	 */
	@Override
	public Optional<Class<?>> getGenericType() {
		return bean.getGenericType();
	}

	/** The bean. */
	private final BeanInfo bean;

	/**
	 * Instantiates a new bean option.
	 *
	 * @param bean the bean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BeanOptionImpl(BeanInfo bean) {
		super(bean.getOptionName(), (Class) bean.getType(), bean.isOptional());
		this.bean = bean;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	@Override
	public void setValue(Object value) {
		final Object processed = accumulator.accumulate(bean.getValue(), value);

		this.bean.setValue(processed);

		super.store(processed);
	}

	/**
	 * To string additions.
	 *
	 * @return the string
	 */
	@Override
	protected String toStringAdditions() {
		return ", " + bean.toString();
	}

	@Override
	public BeanInfo getBeanInfo() {
		return bean;
	}

}
