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
