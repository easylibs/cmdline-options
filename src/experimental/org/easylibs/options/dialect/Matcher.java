package org.easylibs.options.dialect;

import java.util.List;
import java.util.Optional;

import org.easylibs.experimental.ComplexOption;

public interface Matcher {

	Optional<Match> findMatch(ComplexOption<?> option);

	List<String> matched();

	List<String> unmatched();

	List<String> args();
}
