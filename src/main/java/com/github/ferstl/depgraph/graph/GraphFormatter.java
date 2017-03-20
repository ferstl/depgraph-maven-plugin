package com.github.ferstl.depgraph.graph;

import java.util.Collection;

/**
 * Format the given nodes and edges to a string representing the graph.
 */
public interface GraphFormatter {

  String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges);
}
