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
