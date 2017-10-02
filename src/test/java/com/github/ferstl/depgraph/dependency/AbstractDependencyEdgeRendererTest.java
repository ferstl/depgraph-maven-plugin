package com.github.ferstl.depgraph.dependency;

import org.junit.Test;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNodeWithConflict;
import static org.junit.Assert.assertEquals;

public abstract class AbstractDependencyEdgeRendererTest {

  @Test
  public final void renderWithoutVersion() {
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
  public final void renderWithNonConflictingVersion() {
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
  public final void renderWithConflictingVersion() {
    // arrange
    EdgeRenderer<DependencyNode> renderer = createEdgeRenderer(true);
    DependencyNode from = createDependencyNode("group1", "artifact1", "version1");
    DependencyNode to = createDependencyNodeWithConflict("group2", "artifact2", "version2");

    // act
    String result = renderer.render(from, to);

    // assert
    assertEquals(renderWithConflictingVersionResult(), result);
  }

  protected abstract EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion);

  protected abstract String renderWithoutVersionResult();

  protected abstract String renderWithNonConflictingVersionResult();

  protected abstract String renderWithConflictingVersionResult();
}
