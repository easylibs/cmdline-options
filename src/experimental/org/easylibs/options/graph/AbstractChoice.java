package org.easylibs.options.graph;

import org.easylibs.experimental.Choice;

public class AbstractChoice extends BranchNode implements Choice {

	private String name;

	public AbstractChoice(String name) {
		super();

		this.setName(name);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isPresent() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void setName(String name) {
		this.name = name;
	}

}
