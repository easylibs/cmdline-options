package org.easylibs.options;

import java.util.Properties;

class PropertyOptionImpl extends SimpleOption<String> implements PropertyOption {

	private final Properties properties;

	public PropertyOptionImpl(Properties properties, String name) {
		super(name, String.class, true);
		this.properties = properties;
	}

	@Override
	public void setValue(String value) {

		properties.setProperty(getName(), (value == null) ? "1" : value);

		super.setValue(value);
	}

	/**
	 * To string additions.
	 *
	 * @return the string
	 */
	@Override
	protected String toStringAdditions() {
		return ", Properties [" + getName() + "]";
	}

}
