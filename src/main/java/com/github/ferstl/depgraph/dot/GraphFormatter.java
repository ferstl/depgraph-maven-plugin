package com.github.ferstl.depgraph.dot;

import java.util.Collection;

public interface GraphFormatter {

  String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges);
}
