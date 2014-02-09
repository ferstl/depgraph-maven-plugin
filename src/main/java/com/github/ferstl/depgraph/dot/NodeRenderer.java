package com.github.ferstl.depgraph.dot;

import org.apache.maven.shared.dependency.graph.DependencyNode;


public interface NodeRenderer {
  String render(DependencyNode node);
}
