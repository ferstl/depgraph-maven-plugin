package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.EdgeRenderer;
import com.github.ferstl.depgraph.graph.GmlGraphFormatter;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class GmlGraphStyleConfigurer implements GraphStyleConfigurer {

  private boolean showGroupId;
  private boolean showArtifactId;
  private boolean showVersionsOnNodes;
  private boolean showVersionOnEdges;

  @Override
  public GraphStyleConfigurer showGroupIds(boolean showGroupId) {
    this.showGroupId = showGroupId;
    return this;
  }

  @Override
  public GraphStyleConfigurer showArtifactIds(boolean showArtifactId) {
    this.showArtifactId = showArtifactId;
    return this;
  }

  @Override
  public GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes) {
    this.showVersionsOnNodes = showVersionsOnNodes;
    return this;
  }

  @Override
  public GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges) {
    this.showVersionOnEdges = showVersionOnEdges;
    return this;
  }

  @Override
  public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
    NodeRenderer<DependencyNode> nodeNameRenderer = new SimpleDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showVersionsOnNodes);
    EdgeRenderer<DependencyNode> edgeRenderer = new SimpleDependencyEdgeRenderer(this.showVersionOnEdges);
    return graphBuilder
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer)
        .graphFormatter(new GmlGraphFormatter());
  }
}
