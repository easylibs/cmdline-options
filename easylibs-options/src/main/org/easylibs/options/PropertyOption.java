/*
 * 
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
