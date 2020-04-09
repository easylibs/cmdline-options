package org.easylibs.options.dialect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.easylibs.extoptions.ComplexOption;

public class ArgMatch implements Match {

	private final Matcher matcher;
	private final List<String> args;
	private final List<String> matched;
	private final int index;
	private final ComplexOption<?> option;

	public ArgMatch(Matcher matcher, ComplexOption<?> option, List<String> args, int index) {
		this.matcher = matcher;
		this.option = option;
		this.args = args;
		this.index = index;

		this.matched = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(args.get(index))));
	}

	public ArgMatch(Matcher matcher, ComplexOption<?> option, List<String> args, int index, int len) {
		this.matcher = matcher;
		this.option = option;
		this.args = args;
		this.index = index;

		this.matched = Collections.unmodifiableList(args.stream()
				.skip(index)
				.limit(len)
				.collect(Collectors.toList()));
	}

	@Override
	public int size() {
		return matched.size();
	}

	@Override
	public String toString() {
		return "Match ["
//				+ "index=" + index
//				+ ", matched=" + matched
				+ "option='" + option.name() + "'"
				+ "]";
	}

	@Override
	public List<String> matchedArgs() {
		return matched;
	}

	@Override
	public List<String> allArgs() {
		return args;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	@Override
	public ComplexOption<?> option() {
		return option;
	}

}
