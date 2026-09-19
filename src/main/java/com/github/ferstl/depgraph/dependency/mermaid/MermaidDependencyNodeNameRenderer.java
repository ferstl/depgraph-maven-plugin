/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.mermaid;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.dependency.dot.style.StyleKey;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;
import org.apache.maven.artifact.Artifact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;


public class MermaidDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();
  private static final Joiner NEWLINE_JOINER = Joiner.on("<br/>").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;
  private final boolean showScope;

  public MermaidDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
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

    Collection<String> items = new ArrayList<>();
    if (node.getArtifact().isOptional() && showOptional) {
      items.add(printWithSmallFont("#lt;optional#gt;"));
    }
    if (showGroupId) {
      items.add(node.getArtifact().getGroupId());
    }
    if (showArtifactId) {
      items.add(node.getArtifact().getArtifactId());
    }
    if (showVersion) {
      items.add(node.getEffectiveVersion());
    }
    if (showTypes) {
      items.add(createTypeString(node.getTypes()));
    }
    if (showClassifiers) {
      items.add(createClassifierString(node.getClassifiers()));
    }
    if (showScope) {
      items.add(createScopeString(node.getScopes()));
    }
    if (items.isEmpty()) {
      return "[\" \"]";
    }

    return "[\"" + NEWLINE_JOINER.join(items) + "\"]";
  }

  private static String createScopeString(Set<String> scopes) {
    if (!scopes.isEmpty() && (scopes.size() > 1 || !scopes.contains(SCOPE_COMPILE))) {
      return printWithSmallFont("(" + SLASH_JOINER.join(scopes) + ")");
    }

    return null;
  }

  private static String createTypeString(Set<String> types) {
    if (types.size() > 1 || !types.contains("jar")) {
      types.forEach(type -> type = "." + type);

      return SLASH_JOINER.join(types.stream().map(type -> "." + type).collect(Collectors.toList()));
    }

    return null;
  }

  private static String createClassifierString(Set<String> classifiers) {
    if( classifiers.size() > 1 ) {
      return SLASH_JOINER.join(classifiers);
    }

    return null;
  }

  private static String printWithSmallFont(String stringToPrint) {
    return "<font size=1>" + stringToPrint + "</font>";
  }
}

