package org.easylibs.experimental;

import org.easylibs.options.Registration;
import org.easylibs.options.graph.GroupNode;
import org.easylibs.options.graph.Node;

public interface Group extends Choice, Node {

	static Group of(String name) {
		return new GroupNode(name);
	}

	Registration addAction(Action action);

	<T> ComplexOption<T> add(ComplexOption<T> option);

	Group add(Group option);

}
