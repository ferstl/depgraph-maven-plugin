package com.github.ferstl.depgraph.dependency;


import com.github.ferstl.depgraph.dependency.style.StyleConfiguration;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.dot.DotGraphFormatter;

public class DotGraphStyleConfigurer implements GraphStyleConfigurer {

  private final StyleConfiguration styleConfiguration;
  private boolean showGroupId;
  private boolean showArtifactId;
  private boolean showVersionsOnNodes;
  private boolean showVersionOnEdges;

  public DotGraphStyleConfigurer(StyleConfiguration styleConfiguration) {
    this.styleConfiguration = styleConfiguration;
  }

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
    DotDependencyNodeNameRenderer nodeNameRenderer = new DotDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showVersionsOnNodes, this.styleConfiguration);
    DotDependencyEdgeRenderer edgeRenderer = new DotDependencyEdgeRenderer(this.showVersionOnEdges, this.styleConfiguration);

    return graphBuilder
        .graphFormatter(new DotGraphFormatter(this.styleConfiguration.defaultNodeAttributes(), this.styleConfiguration.defaultEdgeAttributes()))
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer);
  }
}
