package org.easylibs.options.dialect;

import java.util.Optional;

public interface Dialect {

	static boolean isNameValid(String name) {
		switch (name) {
		case "posix":
		case "regex":
			return true;
		}

		return false;
	}

	static Dialect of(String name, String format) {

		switch (name) {
		case "posix":
			return new PosixDialect();

		case "regex":
			return new RegexDialect(format);
		}

		throw new IllegalArgumentException("dialect not found '" + name + "'");
	}

	String optionName(String definition);

	Optional<String> valueName(String definition);

	boolean isOptionMatch(String arg, String undecorated);

	boolean isOption(String arg);
}
