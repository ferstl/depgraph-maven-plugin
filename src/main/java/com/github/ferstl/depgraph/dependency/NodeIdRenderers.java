/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;


public enum NodeIdRenderers implements NodeRenderer<DependencyNode> {

  GROUP_ID {
    @Override
    public String render(DependencyNode node) {
      return node.getArtifact().getGroupId();
    }
  },

  GROUP_ID_WITH_SCOPE {
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
          artifact.getClassifier());
    }
  },

  VERSIONLESS_ID_WITH_SCOPE {
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

  },
  
  ID {
    @Override
    public String render(DependencyNode node) {
      Artifact artifact = node.getArtifact();
      return COLON_JOINER.join(
          artifact.getGroupId(),
          artifact.getArtifactId(),
          artifact.getVersion(),
          artifact.getType(),
          artifact.getClassifier());
    }
  };

  private static final Joiner COLON_JOINER = Joiner.on(":").useForNull("");
}
