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
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A group of options.
 */
public interface OptionGroup {

	Optional<Option<?>> find(String name);

	<T> Optional<Option<T>> find(String name, Class<T> type);

	Option<?> get(String name) throws OptionNotFoundException;

	<T> Option<T> get(String name, Class<T> type) throws OptionNotFoundException;

	List<Option<?>> getAll(Predicate<Option<?>> predicate);

	List<Option<?>> getAllMatched() throws OptionNotFoundException;

	List<Option<?>> getAllNamed(Predicate<Option<?>> predicate, String... names) throws OptionNotFoundException;

	List<Option<?>> getAllNamed(String... names) throws OptionNotFoundException;

	List<Option<?>> getAllUnmatched();

	<T> Optional<Option<T>> findMatched(String name, Class<T> type);

	Stream<? extends Option<?>> stream();

}
