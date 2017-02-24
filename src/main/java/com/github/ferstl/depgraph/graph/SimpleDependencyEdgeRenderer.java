package com.github.ferstl.depgraph.graph;

import com.github.ferstl.depgraph.dot.EdgeRenderer;

public class SimpleDependencyEdgeRenderer implements EdgeRenderer<GraphNode> {

  private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
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

  private String abbreviateVersion(String version) {
    if (version.endsWith(SNAPSHOT_SUFFIX)) {
      return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length()) + "-S.";
    }

    return version;
  }
}
