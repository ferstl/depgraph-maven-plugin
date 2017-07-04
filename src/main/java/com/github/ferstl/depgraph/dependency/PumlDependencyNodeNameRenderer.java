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
