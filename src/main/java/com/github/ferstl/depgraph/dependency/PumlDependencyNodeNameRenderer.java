package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class PumlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;

  public PumlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();
    StringBuilder name = new StringBuilder();
    PumlNodeInfo nodeInfo = new PumlNodeInfo().withComponent("rectangle");

    if (this.showGroupId) {
      name.append(artifact.getGroupId());
    }

    if (this.showArtifactId) {
      if (this.showGroupId) {
        name.append(":");
      }
      name.append(artifact.getArtifactId());
    }

    if (this.showVersion) {
      if (this.showGroupId || this.showArtifactId) {
        name.append(":");
      }
      name.append(artifact.getVersion());
    }

    nodeInfo.withLabel(name.toString())
        .withStereotype(node.getArtifact().getScope());

    return nodeInfo.toString();
  }
}
