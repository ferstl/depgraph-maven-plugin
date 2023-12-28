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

import com.github.ferstl.depgraph.dependency.AbstractDependencyNodeNameRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class TextDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
    return new TextDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion, showOptional, showScope);
  }

  @Override
  protected String renderNothingResult() {
    return "";
  }

  @Override
  protected String renderGroupIdResult() {
    return "groupId:compile";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "artifactId:compile";
  }

  @Override
  protected String renderVersionResult() {
    return "version:compile";
  }

  @Override
  protected String renderOptionalResult() {
    return "compile (optional)";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "groupId:artifactId:version:compile";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionOptionalResult() {
    return "groupId:artifactId:version:compile (optional)";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "groupId:artifactId:compile";
  }

  @Override
  protected String renderGroupIdArtifactIdOptionalResult() {
    return "groupId:artifactId:compile (optional)";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "groupId:version:compile";
  }

  @Override
  protected String renderGroupIdVersionOptionalResult() {
    return "groupId:version:compile (optional)";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "artifactId:version:compile";
  }

  @Override
  protected String renderArtifactIdVersionOptionalResult() {
    return "artifactId:version:compile (optional)";
  }

  @Override
  protected String renderTypesResult() {
    return "artifactId:jar/zip:compile";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "artifactId:jar:compile";
  }

  @Override
  protected String renderClassifiersResult() {
    return "artifactId:classifier1/classifier2:compile";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "artifactId::compile";
  }

  @Override
  protected String renderAllResult() {
    return "groupId:artifactId:version:jar/tar.gz/zip:classifier1/classifier2:test";
  }

  @Override
  protected String renderOmitScopeResult() {
    return "groupId:artifactId:version";
  }
}
