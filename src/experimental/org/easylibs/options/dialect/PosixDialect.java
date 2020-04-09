package org.easylibs.options.dialect;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PosixDialect implements Dialect {

	Pattern valueName = Pattern.compile("[\\s=@]+(\\S+)");

	public PosixDialect() {
	}

	@Override
	public String optionName(String definition) {
		return definition
				.replaceFirst("^[-]+", "")
				.replaceFirst("[\\s=\\{\\[][\\S]+", "");
	}

	@Override
	public Optional<String> valueName(String definition) {

		Matcher matcher = valueName.matcher(definition);

		if (matcher.find()) {
			return Optional.ofNullable(matcher.group(1));
		}

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
