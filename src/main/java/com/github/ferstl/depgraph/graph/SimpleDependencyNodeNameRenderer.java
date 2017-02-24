package com.github.ferstl.depgraph.graph;

import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.google.common.base.Joiner;

public class SimpleDependencyNodeNameRenderer implements NodeRenderer<GraphNode> {

  private static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;

  public SimpleDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
  }

  @Override
  public String render(GraphNode node) {
    return COLON_JOINER.join(
        this.showGroupId ? node.getArtifact().getGroupId() : null,
        this.showArtifactId ? node.getArtifact().getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null);
  }
}
