package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class DependencyNodeIdRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

  private boolean withGroupId;
  private boolean withArtifactId;
  private boolean withType;
  private boolean withClassifier;
  private boolean withScope;

  private DependencyNodeIdRenderer() {
  }

  public static DependencyNodeIdRenderer groupId() {
    return new DependencyNodeIdRenderer()
        .withGroupId();
  }

  public static DependencyNodeIdRenderer versionlessId() {
    return new DependencyNodeIdRenderer()
        .withGroupId()
        .withArtifactId()
        .withClassifier();
  }

  public DependencyNodeIdRenderer withGroupId() {
    this.withGroupId = true;
    return this;
  }

  public DependencyNodeIdRenderer withArtifactId() {
    this.withArtifactId = true;
    return this;
  }

  public DependencyNodeIdRenderer withType() {
    this.withType = true;
    return this;
  }

  public DependencyNodeIdRenderer withClassifier() {
    this.withClassifier = true;
    return this;
  }

  public DependencyNodeIdRenderer withScope() {
    this.withScope = true;
    return this;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();

    return COLON_JOINER.join(
        this.withGroupId ? artifact.getGroupId() : null,
        this.withArtifactId ? artifact.getArtifactId() : null,
        this.withType ? artifact.getType() : null,
        this.withClassifier ? artifact.getClassifier() : null,
        this.withScope ? artifact.getScope() : null);
  }
}
