/*
 * 
 */
package org.easylibs.options;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidArgException.
 */
public class InvalidArgException extends ArgException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -101724589531959192L;
	
	/** The option. */
	private final Option<?> option;

	/**
	 * Instantiates a new invalid arg exception.
	 *
	 * @param args    the args
	 * @param message the message
	 * @param option  the option
	 */
	public InvalidArgException(Args args, String message, Option<?> option) {
		super(args, message);
		this.option = option;
	}

	/**
	 * Instantiates a new invalid arg exception.
	 *
	 * @param args    the args
	 * @param message the message
	 * @param option  the option
	 * @param cause   the cause
	 */
	public InvalidArgException(Args args, String message, Option<?> option, Throwable cause) {
		super(args, message + ": " + cause.getClass().getSimpleName() + " [" + cause.getMessage() + "]", cause);
		this.option = option;
	}

	/**
	 * Gets the option.
	 *
	 * @return the option
	 */
	public Option<?> getOption() {
		return option;
	}

}
