package com.github.ferstl.depgraph.graph;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.dot.EdgeRenderer;
import com.github.ferstl.depgraph.dot.GmlGraphFormatter;
import com.github.ferstl.depgraph.dot.NodeRenderer;

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
  public DotBuilder<GraphNode> configure(DotBuilder<GraphNode> graphBuilder) {
    NodeRenderer<GraphNode> nodeNameRenderer = new SimpleDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showVersionsOnNodes);
    EdgeRenderer<GraphNode> edgeRenderer = new SimpleDependencyEdgeRenderer(this.showVersionOnEdges);
    return graphBuilder
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer)
        .graphFormatter(new GmlGraphFormatter());
  }
}
