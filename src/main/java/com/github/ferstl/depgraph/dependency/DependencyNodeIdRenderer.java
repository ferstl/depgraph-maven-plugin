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
package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

import static com.google.common.base.Strings.emptyToNull;

public class DependencyNodeIdRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner COLON_JOINER = Joiner.on(":").skipNulls();

  private boolean withGroupId;
  private boolean withArtifactId;
  private boolean withType;
  private boolean withClassifier;
  private boolean withScope;

  private DependencyNodeIdRenderer() {
  }

  public static DependencyNodeIdRenderer groupId() {
    return new DependencyNodeIdRenderer()
        .withGroupId(true);
  }

  public static DependencyNodeIdRenderer versionlessId() {
    return new DependencyNodeIdRenderer()
        .withGroupId(true)
        .withArtifactId(true);
  }

  public DependencyNodeIdRenderer withClassifier(boolean withClassifier) {
    this.withClassifier = withClassifier;
    return this;
  }

  public DependencyNodeIdRenderer withType(boolean withType) {
    this.withType = withType;
    return this;
  }

  public DependencyNodeIdRenderer withScope(boolean withScope) {
    this.withScope = withScope;
    return this;
  }

  private DependencyNodeIdRenderer withGroupId(boolean withGroupId) {
    this.withGroupId = withGroupId;
    return this;
  }

  private DependencyNodeIdRenderer withArtifactId(boolean withArtifactId) {
    this.withArtifactId = withArtifactId;
    return this;
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();

    return COLON_JOINER.join(
        this.withGroupId ? artifact.getGroupId() : null,
        this.withArtifactId ? artifact.getArtifactId() : null,
        this.withType ? artifact.getType() : null,
        this.withClassifier ? emptyToNull(artifact.getClassifier()) : null,
        this.withScope ? artifact.getScope() : null);
  }
}
