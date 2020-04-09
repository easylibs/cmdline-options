package org.easylibs.options.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphUtils {

	static List<Node> explodeUp(Node start) {
		final List<Node> list = new ArrayList<>();

		return list;
	}

	static boolean explodeUp(Node node, List<Node> list) {

		if (node instanceof BranchNode) {
			return explodeUp((BranchNode) node, list);
		}

		list.add(node);
		return true;
	}

	static boolean explodeUp(BranchNode node, List<Node> list) {
		list.add(node);

		if (node.nodes != null) {
			node.nodes.forEach(n -> explodeUp(n, list));
		}

		return true;
	}

	private GraphUtils() {
	}

}
