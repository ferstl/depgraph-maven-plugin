/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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

import com.github.ferstl.depgraph.graph.NodeRenderer;

public class GmlDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion) {
    return new GmlDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion);
  }

  @Override
  protected String renderNothingResult() {
    return "";
  }

  @Override
  protected String renderGroupIdResult() {
    return "label \"groupId\"";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "label \"artifactId\"";
  }

  @Override
  protected String renderVersionResult() {
    return "label \"version\"";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "label \"groupId\n"
        + "artifactId\n"
        + "version\"";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "label \"groupId\n"
        + "artifactId\"";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "label \"groupId\n"
        + "version\"";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "label \"artifactId\n"
        + "version\"";
  }

  @Override
  protected String renderTypesResult() {
    return "label \"artifactId\n"
        + ".jar/.zip\"";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return renderEmptyClassifierResult();
  }

  @Override
  protected String renderClassifiersResult() {
    return "label \"artifactId\n"
        + "classifier1/classifier2\"";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "label \"artifactId\n\"";
  }

  @Override
  protected String renderAllResult() {
    return "label \"groupId\n"
        + "artifactId\n"
        + "version\n"
        + ".jar/.tar.gz/.zip\n"
        + "classifier1/classifier2\n"
        + "(test)\"";
  }
}
