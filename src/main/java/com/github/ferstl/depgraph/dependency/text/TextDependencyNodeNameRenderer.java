package com.github.ferstl.depgraph.dependency.text;

import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class TextDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();
  private static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;

  public TextDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersionsOnNodes) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersionsOnNodes;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();

    return COLON_JOINER.join(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null,
        this.showTypes ? SLASH_JOINER.join(node.getTypes()) : null,
        this.showClassifiers ? SLASH_JOINER.join(node.getClassifiers()) : null,
        SLASH_JOINER.join(node.getScopes()));
  }

}
