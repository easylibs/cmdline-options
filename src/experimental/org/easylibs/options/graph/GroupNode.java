package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;

import org.easylibs.experimental.Action;
import org.easylibs.experimental.Choice;
import org.easylibs.experimental.ComplexOption;
import org.easylibs.experimental.Group;
import org.easylibs.options.Registration;

public class GroupNode extends AbstractChoice implements Group {

	private final List<? super Choice> options = new ArrayList<>();

	public GroupNode(String name) {
		super(name);
	}

	@Override
	public Registration addAction(Action action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ComplexOption<T> add(ComplexOption<T> option) {

		options.add(option);
		addNode((LeafNode) option);

		return option;
	}

	@Override
	public Group add(Group group) {
		options.add(group);
		addNode((LeafNode) group);

		return group;
	}

	@Override
	public String toString() {
		return "Group [name='" + name() + "']";
	}

	@Override
	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
