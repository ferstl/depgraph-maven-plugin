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
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.NodeAttributeRenderer;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;


public class DependencyNodeLabelRenderer implements NodeAttributeRenderer<GraphNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;
  private final StyleConfiguration styleConfiguration;

  public DependencyNodeLabelRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion, StyleConfiguration styleConfiguration) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
    this.styleConfiguration = styleConfiguration;
  }


  @Override
  public AttributeBuilder createNodeAttributes(GraphNode node) {
    Artifact artifact = node.getArtifact();
    String scopes = createScopeString(node.getScopes());

    return this.styleConfiguration.nodeAttributes(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? artifact.getVersion() : null,
        artifact.getType(),
        scopes, Iterables.getFirst(node.getScopes(), null));
  }

  private static String createScopeString(Set<String> scopes) {
    if (scopes.size() > 1 || !scopes.contains("compile")) {
      return "(" + SLASH_JOINER.join(scopes) + ")";
    }

    return "";
  }

}
