package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNodeWithConflict;
import static org.junit.Assert.assertEquals;

public class SimpleDependencyEdgeRendererTest {

  @Test
  public void renderWithoutVersion() {
    // arrange
    SimpleDependencyEdgeRenderer renderer = new SimpleDependencyEdgeRenderer(false);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNode("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals("", result);
  }

  @Test
  public void renderWithNonConflictingVersion() {
    // arrange
    SimpleDependencyEdgeRenderer renderer = new SimpleDependencyEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNode("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals("", result);
  }

  @Test
  public void renderWithConflictingVersion() {
    // arrange
    SimpleDependencyEdgeRenderer renderer = new SimpleDependencyEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNodeWithConflict("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals("version2", result);
  }
}
