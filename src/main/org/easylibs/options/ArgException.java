/*
 * 
 */
package org.easylibs.options;

// TODO: Auto-generated Javadoc
/**
 * A checked base exception class for all exceptions related to parsing of the
 * command line arguments.
 */
public class ArgException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2904950813748795633L;

	/** The args. */
	private final Args args;

	/**
	 * Instantiates a new arg exception.
	 *
	 * @param args the args
	 */
	public ArgException(Args args) {
		super();
		this.args = args;
	}

	/**
	 * Instantiates a new arg exception.
	 *
	 * @param args the args
	 * @param arg0 the arg 0
	 * @param arg1 the arg 1
	 * @param arg2 the arg 2
	 * @param arg3 the arg 3
	 */
	public ArgException(Args args, String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		this.args = args;
	}

	/**
	 * Instantiates a new arg exception.
	 *
	 * @param args the args
	 * @param arg0 the arg 0
	 * @param arg1 the arg 1
	 */
	public ArgException(Args args, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.args = args;
	}

	/**
	 * Instantiates a new arg exception.
	 *
	 * @param args the args
	 * @param arg0 the arg 0
	 */
	public ArgException(Args args, String arg0) {
		super(arg0);
		this.args = args;
	}

	/**
	 * Instantiates a new arg exception.
	 *
	 * @param args the args
	 * @param arg0 the arg 0
	 */
	public ArgException(Args args, Throwable arg0) {
		super(arg0);
		this.args = args;
	}

	/**
	 * Gets the args.
	 *
	 * @return the args
	 */
	public Args getArgs() {
		return args;
	}

}
