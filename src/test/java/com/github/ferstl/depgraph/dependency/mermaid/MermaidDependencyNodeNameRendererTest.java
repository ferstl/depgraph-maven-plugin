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

import com.github.ferstl.depgraph.dependency.AbstractDependencyNodeNameRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class MermaidDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
    return new MermaidDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion, showOptional, showScope);
  }

  @Override
  protected String renderNothingResult() {
    return "[\" \"]";
  }

  @Override
  protected String renderGroupIdResult() {
    return "[\"groupId\"]";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "[\"artifactId\"]";
  }

  @Override
  protected String renderVersionResult() {
    return "[\"version\"]";
  }

  @Override
  protected String renderOptionalResult() {
    return "[\"<font size=1>#lt;optional#gt;</font>\"]";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "[\"groupId<br/>artifactId<br/>version\"]";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionOptionalResult() {
    return "[\"<font size=1>#lt;optional#gt;</font><br/>groupId<br/>artifactId<br/>version\"]";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "[\"groupId<br/>artifactId\"]";
  }

  @Override
  protected String renderGroupIdArtifactIdOptionalResult() {
    return "[\"<font size=1>#lt;optional#gt;</font><br/>groupId<br/>artifactId\"]";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "[\"groupId<br/>version\"]";
  }

  @Override
  protected String renderGroupIdVersionOptionalResult() {
    return "[\"<font size=1>#lt;optional#gt;</font><br/>groupId<br/>version\"]";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "[\"artifactId<br/>version\"]";
  }

  @Override
  protected String renderArtifactIdVersionOptionalResult() {
    return "[\"<font size=1>#lt;optional#gt;</font><br/>artifactId<br/>version\"]";
  }

  @Override
  protected String renderTypesResult() {
    return "[\"artifactId<br/>.jar/.zip\"]";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "[\"artifactId\"]";
  }

  @Override
  protected String renderClassifiersResult() {
    return "[\"artifactId<br/>classifier1/classifier2\"]";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "[\"artifactId\"]";
  }

  @Override
  protected String renderAllResult() {
    return "[\"groupId<br/>artifactId<br/>version<br/>.jar/.tar.gz/.zip<br/>classifier1/classifier2<br/><font size=1>(test)</font>\"]";
  }

  @Override
  protected String renderOmitScopeResult() {
    return "[\"groupId<br/>artifactId<br/>version\"]";
  }

}
