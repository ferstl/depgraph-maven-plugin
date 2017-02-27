package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.dot.EdgeRenderer;

import static com.github.ferstl.depgraph.dependency.VersionAbbreviator.abbreviateVersion;

public class SimpleDependencyEdgeRenderer implements EdgeRenderer<GraphNode> {

  private final boolean renderVersion;

  public SimpleDependencyEdgeRenderer(boolean renderVersion) {
    this.renderVersion = renderVersion;
  }

  @Override
  public String render(GraphNode from, GraphNode to) {
    NodeResolution resolution = to.getResolution();

    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersion) {
      return abbreviateVersion(to.getArtifact().getVersion());
    }

    return "";
  }
}
