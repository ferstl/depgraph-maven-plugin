package com.github.ferstl.depgraph.dot;


public interface EdgeStyler {
  String styleEdge(Node from, Node to);
}
