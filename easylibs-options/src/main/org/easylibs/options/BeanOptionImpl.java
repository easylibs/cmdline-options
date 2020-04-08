/*
 * 
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
