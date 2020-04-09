package org.easylibs.extoptions;

import java.util.List;

import org.easylibs.options.dialect.Match;

public interface CompoundOption extends ComplexOption<List<String>> {

	void addMatch(Match match);

	List<Match> matches();

}
