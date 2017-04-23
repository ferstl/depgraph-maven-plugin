package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class GmlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;

  public GmlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
  }

  @Override
  public String render(DependencyNode node) {
    String content = NEWLINE_JOINER.join(
        this.showGroupId ? node.getArtifact().getGroupId() : null,
        this.showArtifactId ? node.getArtifact().getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null);

    if (content.isEmpty()) {
      return "";
    }

    return "label \"" + content + "\"";
  }
}
