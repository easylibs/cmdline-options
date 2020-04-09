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
package org.easylibs.getopt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Definition of a long form argument.
 * 
 * @see <a href="https://linux.die.net/man/3/getopt">getopt_long(3)</a>
 * 
 */
public final class PosixLongOption {

	/**
	 * The Enum HasArg.
	 */
	public enum HasArg {

		/** Option has no argument. */
		NO(""),

		/** Option has a required argument. */
		REQUIRED(":"),

		/** Option has an optional argument. */
		OPTIONAL("::");

		/** The shortcardinality. */
		private final String shortcardinality;

		/**
		 * Instantiates a new checks for arg.
		 *
		 * @param shortcardinality the shortcardinality
		 */
		private HasArg(String shortcardinality) {
			this.shortcardinality = shortcardinality;
		}

		/**
		 * Cardinality string definition for getopts for an option's argument. A string
		 * {@code ""} indicates no argument, a string {@code ":"} indicates the argument
		 * is required and a string {@code "::"} indicates the argument is optional.
		 *
		 * @return the options' cardinality string definition
		 */
		public String card() {
			return shortcardinality;
		}
	}

	/**
	 * A short code option, a single char, that specifies that no short code exists
	 * for this long option.
	 */
	public final static char NO_SHORT_CODE = 0;

	/** The name. */
	private final String name;

	/** The has arg. */
	private final HasArg hasArg;

	/** The shortcode. */
	private final char shortcode;

	/** The opaque. */
	private Optional<Object> opaque = Optional.empty();

	/**
	 * Instantiates a new posix long option.
	 *
	 * @param name   the name
	 * @param hasArg the has arg
	 */
	public PosixLongOption(String name, HasArg hasArg) {
		this(name, hasArg, NO_SHORT_CODE);
	}

	/**
	 * Instantiates a new posix long option.
	 *
	 * @param name      the name
	 * @param hasArg    the has arg
	 * @param shortcode the shortcode
	 */
	public PosixLongOption(String name, HasArg hasArg, char shortcode) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(hasArg, "hasArg");

		this.name = name;
		this.hasArg = hasArg;
		this.shortcode = shortcode;

	}

	/**
	 * Sets the opaque.
	 *
	 * @param <U> the generic type
	 * @param obj the new opaque
	 */
	public <U> void setOpaque(U obj) {
		this.opaque = Optional.ofNullable(obj);
	}

	/**
	 * Gets the opaque.
	 *
	 * @param <U>  the generic type
	 * @param type the type
	 * @return the opaque
	 */
	@SuppressWarnings("unchecked")
	public <U> Optional<U> getOpaque(Class<U> type) {
		return (Optional<U>) this.opaque;
	}

	/**
	 * On opaque.
	 *
	 * @param <U>    the generic type
	 * @param type   the type
	 * @param action the action
	 */
	@SuppressWarnings("unchecked")
	public <U> void onOpaque(Class<U> type, Consumer<U> action) {
		((Optional<U>) opaque).ifPresent(action);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the checks for arg.
	 *
	 * @return the checks for arg
	 */
	public HasArg getHasArg() {
		return hasArg;
	}

	/**
	 * Checks if is short code present.
	 *
	 * @return true, if is short code present
	 */
	public boolean isShortCodePresent() {
		return shortcode != NO_SHORT_CODE;
	}

	/**
	 * Gets the short code.
	 *
	 * @return the short code
	 */
	public char getShortCode() {
		return shortcode;
	}

	/**
	 * Checks for argument.
	 *
	 * @return true, if successful
	 */
	public boolean hasArgument() {
		return (hasArg != HasArg.NO);
	}

	/**
	 * Checks if is argument optional.
	 *
	 * @return true, if is argument optional
	 */
	public boolean isArgumentOptional() {
		return (hasArg == HasArg.OPTIONAL);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "PosixLongOption [name=" + name + ", hasArg=" + hasArg + "]";
	}

}
