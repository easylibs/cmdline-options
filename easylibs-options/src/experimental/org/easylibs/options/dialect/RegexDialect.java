package org.easylibs.options.dialect;

import java.util.Optional;
import java.util.regex.Pattern;

public class RegexDialect implements Dialect {

	private Pattern pattern;

	private PosixDialect posix = new PosixDialect();

	public RegexDialect(String format) {
		this.pattern = Pattern.compile(format);
	}

	@Override
	public String optionName(String definition) {
		return posix.optionName(definition);
	}

	@Override
	public Optional<String> valueName(String definition) {
		return Optional.empty();
	}

	@Override
	public boolean isOptionMatch(String arg, String undecorated) {
		return optionName(arg).equals(undecorated);
	}

	@Override
	public boolean isOption(String arg) {
		return arg.matches("^[-]+");
	}

}
