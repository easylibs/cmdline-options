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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easylibs.options.TypeMapper;
import org.easylibs.options.dialect.Dialect;
import org.easylibs.options.graph.OptionNode;

public interface ComplexOption<T> extends Choice, HasOptionName {

	static <T> ComplexOption<T> empty() {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<String> fromDefinition(String definition) {
		throw new UnsupportedOperationException();
	}

	static <T> ComplexOption<T> of(String name, Class<T> type) {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<?> of(String name, Class<?>... parameterTypes) {
		throw new UnsupportedOperationException();
	}

	static <T, C extends List<?>> ComplexOption<List<T>> ofList(
			String name,
			Class<T> parameterType) {
		return new OptionNode<>(name, ArrayList::new);
	}

	static <T> ComplexOption<T> of(String name, TypeMapper<T> mapper) {
		throw new UnsupportedOperationException();
	}

	static ComplexOption<?> of(String name, TypeMapper<?>... mapper) {
		throw new UnsupportedOperationException();
	}

	<U> void setType(Class<U> type, TypeMapper<U> typeMapper);

	<U> void setType(Class<U> type);

	void min(int min);

	void max(int max);

	int min();

	int max();

	boolean isCompound();

	Optional<T> value();

	Optional<String> valueName();

	Optional<Dialect> dialect();
}
