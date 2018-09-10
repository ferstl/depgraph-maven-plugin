/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.dot;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.dependency.dot.style.StyleKey;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;


public class DotDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;
  private final StyleConfiguration styleConfiguration;

  public DotDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, StyleConfiguration styleConfiguration) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersion;
    this.showOptional = showOptional;
    this.styleConfiguration = styleConfiguration;
  }


  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();
    String scopes = createScopeString(node.getScopes());
    String types = createTypeString(node.getTypes());
    String classifiers = createClassifierString(node.getClassifiers());

    String effectiveScope = node.getEffectiveScope();
    StyleKey styleKey = StyleKey.create(artifact.getGroupId(), artifact.getArtifactId(), effectiveScope, artifact.getType(), node.getEffectiveVersion(), classifiers, artifact.isOptional());

    return this.styleConfiguration.nodeAttributes(
        styleKey,
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null,
        this.showOptional && artifact.isOptional(),
        this.showTypes ? types : null,
        this.showClassifiers ? classifiers : null,
        scopes
    ).toString();
  }

  private static String createScopeString(Set<String> scopes) {
    if (scopes.size() > 1 || !scopes.contains("compile")) {
      return "(" + SLASH_JOINER.join(scopes) + ")";
    }

    return "";
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

  private static String createClassifierString(Set<String> classifiers) {
    return SLASH_JOINER.join(classifiers);
  }

}
