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
package org.easylibs.experimental;

import java.lang.reflect.Field;

public interface BeanOption extends ComplexOption<Object> {

	static BeanOption of(String name, Class<?> cl) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption of(String name, Object obj) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(Field field) {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(Field field, Object obj) {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(String name, Class<?> cl) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(String name, Object obj) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofMethod(String name, Object obj, Class<?>... parameterTypes)
			throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

}
