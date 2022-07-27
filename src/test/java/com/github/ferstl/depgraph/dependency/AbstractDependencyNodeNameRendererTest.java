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
package com.github.ferstl.depgraph.dependency;

import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addClassifiers;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addTypes;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractDependencyNodeNameRendererTest {

  @Test
  final void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, false, false, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderNothingResult(), result);
  }

  @Test
  final void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, false, false, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdResult(), result);
  }

  @Test
  final void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderArtifactIdResult(), result);
  }

  @Test
  final void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, false, false, false, true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderVersionResult(), result);
  }

  @Test
  final void renderOptional() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", true);
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, false, false, false, false, true, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderOptionalResult(), result);
  }

  @Test
  final void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdVersionResult(), result);
  }

  @Test
  final void renderGroupIdArtifactIdVersionOptional() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", true);
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, true, true, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdVersionOptionalResult(), result);
  }

  @Test
  final void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdResult(), result);
  }

  @Test
  final void renderGroupIdArtifactIdOptional() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", true);
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, false, true, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdOptionalResult(), result);
  }

  @Test
  final void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, false, false, false, true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdVersionResult(), result);
  }

  @Test
  final void renderGroupIdVersionOptional() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", true);
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, false, false, false, true, true, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdVersionOptionalResult(), result);
  }

  @Test
  final void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, false, true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderArtifactIdVersionResult(), result);
  }

  @Test
  final void renderArtifactIdVersionOptional() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", true);
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, false, true, true, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderArtifactIdVersionOptionalResult(), result);
  }

  @Test
  final void renderTypes() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar", "zip");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, true, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderTypesResult(), result);
  }

  @Test
  final void renderJarTypeOnly() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, true, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderJarTypeOnlyResult(), result);
  }

  @Test
  final void renderClassifiers() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "classifier1", "classifier2");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, true, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderClassifiersResult(), result);
  }

  @Test
  final void renderEmptyClassifier() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, true, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderEmptyClassifierResult(), result);
  }

  @Test
  final void renderAll() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", "test");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, true, true, true, false, true);
    addClassifiers(node, "classifier1", "classifier2");
    addTypes(node, "jar", "zip", "tar.gz");

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderAllResult(), result);
  }

  @Test
  final void omitScope() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", "test");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderOmitScopeResult(), result);
  }

  protected abstract NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope);

  protected abstract String renderNothingResult();

  protected abstract String renderGroupIdResult();

  protected abstract String renderArtifactIdResult();

  protected abstract String renderVersionResult();

  protected abstract String renderOptionalResult();

  protected abstract String renderGroupIdArtifactIdVersionResult();

  protected abstract String renderGroupIdArtifactIdVersionOptionalResult();

  protected abstract String renderGroupIdArtifactIdResult();

  protected abstract String renderGroupIdArtifactIdOptionalResult();

  protected abstract String renderGroupIdVersionResult();

  protected abstract String renderGroupIdVersionOptionalResult();

  protected abstract String renderArtifactIdVersionResult();

  protected abstract String renderArtifactIdVersionOptionalResult();

  protected abstract String renderTypesResult();

  protected abstract String renderJarTypeOnlyResult();

  protected abstract String renderClassifiersResult();

  protected abstract String renderEmptyClassifierResult();

  protected abstract String renderAllResult();

  protected abstract String renderOmitScopeResult();
}
