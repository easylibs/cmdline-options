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
