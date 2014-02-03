package com.github.ferstl.depgraph;

import org.apache.maven.shared.dependency.graph.DependencyNode;


enum ArtifactIdRenderer implements DependencyNodeRenderer {
  INSTANCE;

  @Override
  public String render(DependencyNode node) {
    return node.getArtifact().getArtifactId();
  }

}
