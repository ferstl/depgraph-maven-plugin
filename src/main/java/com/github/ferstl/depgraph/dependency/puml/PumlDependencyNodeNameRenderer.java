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
package com.github.ferstl.depgraph.dependency.puml;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class PumlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\\n").skipNulls();
  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;

  public PumlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersion;
    this.showOptional = showOptional;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();
    PumlNodeInfo nodeInfo = new PumlNodeInfo().withComponent("rectangle");

    String name = NEWLINE_JOINER.join(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showTypes ? createTypeString(node.getTypes()) : null,
        this.showClassifiers ? createClassifierString(node.getClassifiers()) : null,
        this.showVersion ? node.getEffectiveVersion() : null);

    nodeInfo
        .withLabel(name)
        .withStereotype(node.getEffectiveScope())
        .withOptional(this.showOptional && artifact.isOptional());

    return nodeInfo.toString();
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
