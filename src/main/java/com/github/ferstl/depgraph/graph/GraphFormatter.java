package com.github.ferstl.depgraph.graph;

import java.util.Collection;

public interface GraphFormatter {

  String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges);
}
