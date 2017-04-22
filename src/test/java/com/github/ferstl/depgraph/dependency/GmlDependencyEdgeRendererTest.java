package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNodeWithConflict;
import static org.junit.Assert.assertEquals;

public class GmlDependencyEdgeRendererTest {

  @Test
  public void renderWithoutVersion() {
    // arrange
    GmlDependencyEdgeRenderer renderer = new GmlDependencyEdgeRenderer(false);
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
    GmlDependencyEdgeRenderer renderer = new GmlDependencyEdgeRenderer(true);
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
    GmlDependencyEdgeRenderer renderer = new GmlDependencyEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNodeWithConflict("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals("label \"version2\"\n"
        + "graphics\n"
        + "[\n"
        + "style \"dashed\"\n"
        + "fill \"#FF0000\"\n"
        + "]", result);
  }
}
