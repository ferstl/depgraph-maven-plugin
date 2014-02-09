package com.github.ferstl.depgraph;

import org.apache.maven.shared.dependency.graph.DependencyNode;


public interface DependencyNodeRenderer {
  String render(DependencyNode node);
}
