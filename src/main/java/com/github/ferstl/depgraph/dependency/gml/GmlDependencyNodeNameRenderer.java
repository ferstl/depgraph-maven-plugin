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
package com.github.ferstl.depgraph.dependency.gml;

import java.util.LinkedHashSet;
import java.util.Set;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;

public class GmlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;
  private final boolean showScope;

  public GmlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersion;
    this.showOptional = showOptional;
    this.showScope = showScope;
  }

  @Override
  public String render(DependencyNode node) {
    String content = NEWLINE_JOINER.join(
        this.showOptional && node.getArtifact().isOptional() ? "<optional>" : null,
        this.showGroupId ? node.getArtifact().getGroupId() : null,
        this.showArtifactId ? node.getArtifact().getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null,
        this.showTypes ? createTypeString(node.getTypes()) : null,
        this.showClassifiers ? createClassifierString(node.getClassifiers()) : null,
        this.showScope ? createScopeString(node.getScopes()) : null);

    if (content.isEmpty()) {
      return "";
    }

    return "label \"" + content + "\"";
  }

  private static String createTypeString(Set<String> types) {
    if (types.size() > 1 || !types.contains("jar")) {
      Set<String> typesToDisplay = new LinkedHashSet<>(types.size());
      for (String type : types) {
        typesToDisplay.add("." + type);
      }

      return SLASH_JOINER.join(typesToDisplay);
    }

    return "";
  }

  private static String createScopeString(Set<String> scopes) {
    if (!scopes.isEmpty() && (scopes.size() > 1 || !scopes.contains(SCOPE_COMPILE))) {
      return "(" + SLASH_JOINER.join(scopes) + ")";
    }

    return null;
  }

  private static String createClassifierString(Set<String> classifiers) {
    return SLASH_JOINER.join(classifiers);
  }
}
