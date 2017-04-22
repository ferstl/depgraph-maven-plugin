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

    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersion) {
      return "label \"" + abbreviateVersion(to.getArtifact().getVersion()) + "\"";
    }

    return "";
  }
}
