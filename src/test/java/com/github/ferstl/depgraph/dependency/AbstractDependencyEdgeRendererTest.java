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
package com.github.ferstl.depgraph.dependency;

import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNodeWithConflict;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNodeWithDuplicate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractDependencyEdgeRendererTest {

  @Test
  final void renderWithoutVersion() {
    // arrange
    EdgeRenderer<DependencyNode> renderer = createEdgeRenderer(false);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNode("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals(renderWithoutVersionResult(), result);
  }

  @Test
  final void renderWithNonConflictingVersion() {
    // arrange
    EdgeRenderer<DependencyNode> renderer = createEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNode("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals(renderWithNonConflictingVersionResult(), result);
  }

  @Test
  final void renderWithConflictShowingVersion() {
    // arrange
    EdgeRenderer<DependencyNode> renderer = createEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNodeWithConflict("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals(renderWithConflictShowingVersionResult(), result);
  }

  @Test
  final void renderWithConflictNotShowingVersion() {
    // arrange
    EdgeRenderer<DependencyNode> renderer = createEdgeRenderer(false);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNodeWithConflict("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals(renderWithConflictNotShowingVersionResult(), result);
  }

  @Test
  final void renderWithDuplicate() {
    // arrange
    EdgeRenderer<DependencyNode> edgeRenderer = createEdgeRenderer(false);

    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode duplicateTo = createDependencyNodeWithDuplicate("group2", "artifact2", "version2");

    // act
    String result = edgeRenderer.render(from, duplicateTo);

    // assert
    assertEquals(renderWithDuplicateResult(), result);
  }

  protected abstract EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion);

  protected abstract String renderWithoutVersionResult();

  protected abstract String renderWithNonConflictingVersionResult();

  protected abstract String renderWithConflictShowingVersionResult();

  protected abstract String renderWithConflictNotShowingVersionResult();

  protected abstract String renderWithDuplicateResult();
}
