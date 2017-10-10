package com.github.ferstl.depgraph.text;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class TextDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean showVersions;

  public TextDependencyEdgeRenderer(boolean showVersionOnEdges) {
    this.showVersions = showVersionOnEdges;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    if (to.getResolution() == NodeResolution.OMITTED_FOR_CONFLICT) {
      String message = "omitted for conflict";
      if (this.showVersions) {
        message += ": " + to.getArtifact().getVersion();
      }

      return message;
    }

    return "";
  }
}
