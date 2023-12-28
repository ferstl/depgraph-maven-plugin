/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.text;

import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;

public class TextDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();
  private static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;
  private final boolean showScope;

  public TextDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersionsOnNodes, boolean showOptional, boolean showScope) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersionsOnNodes;
    this.showOptional = showOptional;
    this.showScope = showScope;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();

    String artifactString = COLON_JOINER.join(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null,
        this.showTypes ? SLASH_JOINER.join(node.getTypes()) : null,
        this.showClassifiers ? SLASH_JOINER.join(node.getClassifiers()) : null,
        this.showScope ? createScopeString(node.getScopes()) : null);

    if (this.showOptional && artifact.isOptional()) {
      return artifactString + " (optional)";
    }

    return artifactString;
  }

  private static String createScopeString(Set<String> scopes) {
    if (scopes.isEmpty()) {
      return SCOPE_COMPILE;
    }

    return SLASH_JOINER.join(scopes);
  }

}
