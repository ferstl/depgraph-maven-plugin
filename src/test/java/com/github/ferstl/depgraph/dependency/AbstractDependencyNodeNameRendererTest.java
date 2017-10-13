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

import org.junit.Test;

import com.github.ferstl.depgraph.graph.NodeRenderer;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addClassifiers;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addTypes;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public abstract class AbstractDependencyNodeNameRendererTest {

  @Test
  public final void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, false, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderNothingResult(), result);
  }

  @Test
  public final void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdResult(), result);
  }

  @Test
  public final void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderArtifactIdResult(), result);
  }

  @Test
  public final void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderVersionResult(), result);
  }

  @Test
  public final void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdVersionResult(), result);
  }

  @Test
  public final void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdArtifactIdResult(), result);
  }

  @Test
  public final void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderGroupIdVersionResult(), result);
  }

  @Test
  public final void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderArtifactIdVersionResult(), result);
  }

  @Test
  public final void renderTypes() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar", "zip");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderTypesResult(), result);
  }

  @Test
  public final void renderJarTypeOnly() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderJarTypeOnlyResult(), result);
  }

  @Test
  public final void renderClassifiers() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "classifier1", "classifier2");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderClassifiersResult(), result);
  }

  @Test
  public final void renderEmptyClassifier() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderEmptyClassifierResult(), result);
  }

  @Test
  public final void renderAll() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", "test");
    NodeRenderer<DependencyNode> renderer = createNodeNameRenderer(true, true, true, true, true);
    addClassifiers(node, "classifier1", "classifier2");
    addTypes(node, "jar", "zip", "tar.gz");

    // act
    String result = renderer.render(node);

    // assert
    assertEquals(renderAllResult(), result);
  }

  protected abstract NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion);

  protected abstract String renderNothingResult();

  protected abstract String renderGroupIdResult();

  protected abstract String renderArtifactIdResult();

  protected abstract String renderVersionResult();

  protected abstract String renderGroupIdArtifactIdVersionResult();

  protected abstract String renderGroupIdArtifactIdResult();

  protected abstract String renderGroupIdVersionResult();

  protected abstract String renderArtifactIdVersionResult();

  protected abstract String renderTypesResult();

  protected abstract String renderJarTypeOnlyResult();

  protected abstract String renderClassifiersResult();

  protected abstract String renderEmptyClassifierResult();

  protected abstract String renderAllResult();
}
