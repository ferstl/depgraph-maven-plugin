/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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

import com.github.ferstl.depgraph.dependency.AbstractDependencyNodeNameRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class PumlDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
    return new PumlDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion, showOptional, showScope);
  }

  @Override
  protected String renderNothingResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"\"}";
  }

  @Override
  protected String renderGroupIdResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderVersionResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"version\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderOptionalResult() {
    return "{\"component\":\"rectangle\",\"optional\":true,\"label\":\"\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\\\\nartifactId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionOptionalResult() {
    return "{\"component\":\"rectangle\",\"optional\":true,\"label\":\"groupId\\\\nartifactId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\\\\nartifactId\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdArtifactIdOptionalResult() {
    return "{\"component\":\"rectangle\",\"optional\":true,\"label\":\"groupId\\\\nartifactId\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderGroupIdVersionOptionalResult() {
    return "{\"component\":\"rectangle\",\"optional\":true,\"label\":\"groupId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderArtifactIdVersionOptionalResult() {
    return "{\"component\":\"rectangle\",\"optional\":true,\"label\":\"artifactId\\\\nversion\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderTypesResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\\\\n.jar/.zip\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\\\\n\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderClassifiersResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\\\\nclassifier1/classifier2\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"artifactId\\\\n\",\"stereotype\":\"compile\"}";
  }

  @Override
  protected String renderAllResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\\\\nartifactId\\\\n.jar/.tar.gz/.zip\\\\nclassifier1/classifier2\\\\nversion\",\"stereotype\":\"test\"}";
  }

  @Override
  protected String renderOmitScopeResult() {
    return "{\"component\":\"rectangle\",\"optional\":false,\"label\":\"groupId\\\\nartifactId\\\\nversion\"}";
  }
}
