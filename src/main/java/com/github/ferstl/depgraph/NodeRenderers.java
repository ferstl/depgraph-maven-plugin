package com.github.ferstl.depgraph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.google.common.base.Joiner;


enum NodeRenderers implements NodeRenderer {
  ARTIFACT_ID {
    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();

      if (!"compile".equals(artifact.getScope())) {
        return artifact.getArtifactId() + "\\n(" + artifact.getScope() + ")";
      }

      return artifact.getArtifactId();
    }
  },

  VERSIONLESS_ID {

    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();

      return COLON_JOINER.join(
          artifact.getGroupId(),
          artifact.getArtifactId(),
          artifact.getType(),
          artifact.getClassifier(),
          artifact.getScope());
    }

  };

  private static final Joiner COLON_JOINER = Joiner.on(":").useForNull("");

}
