/*
 * 
 */
package org.easylibs.options;

// TODO: Auto-generated Javadoc
/**
 * Unchecked base class exception for all option definition related errors. The
 * exceptions of this type are unchecked or runtime exceptions and are optional
 * to handle within the user code.
 * 
 * Option exceptions do no depend on command line argument state and thus once
 * correct options are defined, the compiled code does not need to concern
 * itself with option related exceptions unless the options are modified or if
 * options come from beans the underlying container classes, fields or methods
 * have been modified.
 */
public class OptionException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9019260048578866637L;

	/**
	 * Instantiates a new option exception.
	 */
	public OptionException() {
	}

	/**
	 * Instantiates a new option exception.
	 *
	 * @param message the arg 0
	 */
	public OptionException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new option exception.
	 *
	 * @param cause the arg 0
	 */
	public OptionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new option exception.
	 *
	 * @param message the arg 0
	 * @param cause   the arg 1
	 */
	public OptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
