package com.github.ferstl.depgraph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.google.common.base.Joiner;


enum NodeRenderers implements NodeRenderer {
  ARTIFACT_ID_LABEL {
    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();
      return toScopedString(artifact.getArtifactId(), artifact.getScope());
    }
  },

  GROUP_ID_LABEL {

    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();
      return toScopedString(artifact.getGroupId(), artifact.getScope());
    }

  },

  SCOPED_GROUP_ID {
    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();
      return COLON_JOINER.join(artifact.getGroupId(), artifact.getScope());
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

  private static String toScopedString(String string, String scope) {
    if (!"compile".equals(scope)) {
      return string + "\\n(" + scope + ")";
    }

    return string;
  }

}
