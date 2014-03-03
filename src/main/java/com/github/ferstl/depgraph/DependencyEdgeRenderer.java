package com.github.ferstl.depgraph;

import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.EdgeRenderer;
import com.github.ferstl.depgraph.dot.Node;


class DependencyEdgeRenderer implements EdgeRenderer {

  private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

  private final boolean renderVersions;

  public DependencyEdgeRenderer(boolean renderVersions) {
    this.renderVersions = renderVersions;
  }

  @Override
  public String createEdgeAttributes(Node from, Node to) {
    AttributeBuilder builder = new AttributeBuilder();
    NodeResolution resolution = to.getResolution();

    if (this.renderVersions && resolution != NodeResolution.OMITTED_FOR_DUPLICATE) {
      builder.label(abbreviateVersion(to.getArtifact().getVersion()));
    }

    switch(resolution) {
      case OMITTED_FOR_DUPLICATE:
        return builder.style("dotted").toString();

      case OMMITTED_FOR_CONFLICT:
        return builder.style("dashed").color("red").fontColor("red").toString();

      default:
        return builder.toString();
    }
  }

  private String abbreviateVersion(String version) {
    if (version.endsWith(SNAPSHOT_SUFFIX)) {
      return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length()) + "-S.";
    }

    return version;
  }
}
