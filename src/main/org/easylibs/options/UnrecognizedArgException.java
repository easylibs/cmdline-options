/*
 * 
 */
package org.easylibs.options;

// TODO: Auto-generated Javadoc
/**
 * The Class UnrecognizedArgException.
 */
public class UnrecognizedArgException extends ArgException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6634535995072779078L;
	
	/** The unrecognized arg. */
	private final String unrecognizedArg;

	/**
	 * Instantiates a new unrecognized arg exception.
	 *
	 * @param args            the args
	 * @param message         the message
	 * @param unrecognizedArg the unrecognized arg
	 */
	public UnrecognizedArgException(Args args, String message, String unrecognizedArg) {
		super(args, message);
		this.unrecognizedArg = unrecognizedArg;
	}

	/**
	 * Gets the unrecognized arg.
	 *
	 * @return the unrecognized arg
	 */
	public String getUnrecognizedArg() {
		return unrecognizedArg;
	}

}
