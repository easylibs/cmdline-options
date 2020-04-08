package org.easylibs.extoptions;

import org.easylibs.options.graph.Graph;
import org.easylibs.options.graph.Node;

public interface Rule {

	boolean rule(Graph graph, Node node);
}
