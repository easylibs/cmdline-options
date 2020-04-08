package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;

import org.easylibs.extoptions.Action;
import org.easylibs.extoptions.Choice;
import org.easylibs.extoptions.Group;
import org.easylibs.extoptions.ComplexOption;
import org.easylibs.options.util.Registration;

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
