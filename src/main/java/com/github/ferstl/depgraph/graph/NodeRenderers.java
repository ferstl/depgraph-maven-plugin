/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph.graph;

import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.google.common.base.Joiner;


public enum NodeRenderers implements NodeRenderer<GraphNode> {
  ARTIFACT_ID_LABEL {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      return toScopedString(artifact.getArtifactId(), node.getScopes());
    }
  },

  ARTIFACT_ID_VERSION_LABEL {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      String artifactLabel = artifact.getArtifactId() + "\n" + artifact.getVersion();

      return toScopedString(artifactLabel, node.getScopes());
    }

  },

  GROUP_ID_ARTIFACT_ID_LABEL {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      String artifactLabel = artifact.getGroupId() + "\n" + artifact.getArtifactId();

      return toScopedString(artifactLabel, node.getScopes());
    }

  },

  GROUP_ID_ARTIFACT_ID_VERSION_LABEL {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      String artifactLabel = artifact.getGroupId() + "\n" + artifact.getArtifactId() + "\n" + artifact.getVersion();

      return toScopedString(artifactLabel, node.getScopes());
    }

  },

  GROUP_ID_LABEL {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      return toScopedString(artifact.getGroupId(), node.getScopes());
    }

  },

  SCOPED_GROUP_ID {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();
      return COLON_JOINER.join(artifact.getGroupId(), artifact.getScope());
    }
  },

  VERSIONLESS_ID {

    @Override
    public String render(GraphNode node) {
      Artifact artifact = node.getArtifact();

      return COLON_JOINER.join(
          artifact.getGroupId(),
          artifact.getArtifactId(),
          artifact.getType(),
          artifact.getClassifier());
    }
  },

  VERSIONLESS_ID_WITH_SCOPE {

    @Override
    public String render(GraphNode node) {
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
  private static final Joiner SLASH_JOINER = Joiner.on("/").useForNull("");

  private static String toScopedString(String string, Set<String> scopes) {
    if (scopes.size() > 1 || !scopes.contains("compile")) {
      return string + "\n(" + SLASH_JOINER.join(scopes) + ")";
    }

    return string;
  }

}
