package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class JsonDependencyEdgeRendererTest extends AbstractDependencyEdgeRendererTest {

  @Override
  protected EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion) {
    return new JsonDependencyEdgeRenderer(renderVersion);
  }

  @Override
  protected String renderWithoutVersionResult() {
    return "{\"resolution\":\"INCLUDED\"}";
  }

  @Override
  protected String renderWithNonConflictingVersionResult() {
    return "{\"resolution\":\"INCLUDED\"}";
  }

  @Override
  protected String renderWithConflictingVersionResult() {
    return "{\"version\":\"version2\",\"resolution\":\"OMITTED_FOR_CONFLICT\"}";
  }
}
