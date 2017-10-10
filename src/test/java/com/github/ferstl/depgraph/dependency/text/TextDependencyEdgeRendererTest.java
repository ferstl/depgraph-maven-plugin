package com.github.ferstl.depgraph.dependency.text;

import com.github.ferstl.depgraph.dependency.AbstractDependencyEdgeRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class TextDependencyEdgeRendererTest extends AbstractDependencyEdgeRendererTest {

  @Override
  protected EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion) {
    return new TextDependencyEdgeRenderer(renderVersion);
  }

  @Override
  protected String renderWithoutVersionResult() {
    return "";
  }

  @Override
  protected String renderWithNonConflictingVersionResult() {
    return "";
  }

  @Override
  protected String renderWithConflictingVersionResult() {
    return "omitted for conflict: version2";
  }
}
