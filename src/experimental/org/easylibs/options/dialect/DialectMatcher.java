package org.easylibs.options.dialect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.easylibs.experimental.ComplexOption;

public class DialectMatcher implements Matcher {

	private final List<String> args;
	private final List<String> matched = new ArrayList<>();
	private final List<String> unmatched = new ArrayList<>();

	public DialectMatcher(List<String> args) {
		this.args = Collections.unmodifiableList(args);

		this.matched.clear();
		this.unmatched.addAll(args);
	}

	@Override
	public Optional<Match> findMatch(ComplexOption<?> option) {
		if (!option.dialect().isPresent()) {
			return Optional.empty();
		}

		final Dialect dialect = option.dialect().get();

		for (int i = 0; i < args.size(); i++) {
			final String arg = args.get(i);

			if (dialect.isOptionMatch(arg, option.name())) {
				unmatched.remove(arg);
				matched.add(arg);

				final int start = i + 1;
				final int end = (i + 1 + option.max());
				final int len = matchArgs(dialect, start, (end < args.size()) ? end : args.size());

//				System.out.println("len=" + len + ", start=" + start + ", end=" + end);

				if (len < option.min()) {
					option.valueName()
							.ifPresent(name -> System.out
									.println("missing: value arg '" + name + "' for option '" + arg + "'"));
					return Optional.empty();
				}

				if (len > 0) {
					return Optional.of(new ArgMatch(this, option, args, i, len + 1));
				} else {
					return Optional.of(new ArgMatch(this, option, args, i));
				}
			}
		}

		return Optional.empty();
	}

	private int matchArgs(final Dialect dialect, int start, int end) {

		int i = start;
		for (; i < end; i++) {
			final String arg = args.get(i);

			if (!dialect.isOption(arg)) {
				i++;
				break;
			}
		}

		return i - start;
	}

	@Override
	public List<String> matched() {
		return Collections.unmodifiableList(matched);
	}

	@Override
	public List<String> unmatched() {
		return Collections.unmodifiableList(unmatched);
	}

	@Override
	public List<String> args() {
		return Collections.unmodifiableList(args);
	}

}
