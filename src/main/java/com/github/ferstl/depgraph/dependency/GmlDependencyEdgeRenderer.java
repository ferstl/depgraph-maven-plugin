package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.EdgeRenderer;

import static com.github.ferstl.depgraph.dependency.VersionAbbreviator.abbreviateVersion;

public class GmlDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean renderVersion;

  public GmlDependencyEdgeRenderer(boolean renderVersion) {
    this.renderVersion = renderVersion;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();
    StringBuilder builder = new StringBuilder();

    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT) {
      if (this.renderVersion) {
        builder.append("label \"").append(abbreviateVersion(to.getArtifact().getVersion())).append("\"");
      }

      addStyleAttributes(builder, "dashed", "#FF0000");

    } else if (resolution == NodeResolution.OMITTED_FOR_DUPLICATE) {
      addStyleAttributes(builder, "dotted", "");
    }

    return builder.toString();
  }

  private static void addStyleAttributes(StringBuilder builder, String edgeStyle, String color) {
    builder.append("\n")
        .append("graphics\n")
        .append("[\n")
        .append("style \"").append(edgeStyle).append("\"");

    if (!color.isEmpty()) {
      builder.append("\n")
          .append("fill \"").append(color).append("\"");
    }

    builder.append("\n")
        .append("]");
  }
}
