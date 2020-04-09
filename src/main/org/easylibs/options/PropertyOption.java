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
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Property option that gets its name and maps its value to a java
 * {@link Properties} object.
 */
public interface PropertyOption extends Option<String> {

	/**
	 * Create a list of options for each property found in the suplied
	 * {@code Properties} object. Each property key becomes an option. When option
	 * value is set, it is also set within the container properties object for that
	 * same key.
	 *
	 * @param properties the java properties to scan for option names and where the
	 *                   option values will be stored
	 * @return a new list of property based options
	 */
	public static List<Option<?>> listOptions(Properties properties) {
		final Set<String> en = properties.stringPropertyNames();

		return en.stream()
				.map(name -> new PropertyOptionImpl(properties, name))
				.collect(Collectors.toList());
	}

}
