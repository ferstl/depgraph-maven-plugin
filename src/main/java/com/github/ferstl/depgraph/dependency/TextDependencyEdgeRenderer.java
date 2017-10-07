package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class TextDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  public TextDependencyEdgeRenderer(boolean showVersionOnEdges) {
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    return "";
  }
}
