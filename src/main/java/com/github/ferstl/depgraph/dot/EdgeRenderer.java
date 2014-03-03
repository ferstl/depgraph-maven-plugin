package com.github.ferstl.depgraph.dot;


public interface EdgeRenderer {
  String createEdgeAttributes(Node from, Node to);
}
