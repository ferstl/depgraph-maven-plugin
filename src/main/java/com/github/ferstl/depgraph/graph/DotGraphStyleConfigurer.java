package com.github.ferstl.depgraph.graph;


import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;

public class DotGraphStyleConfigurer implements GraphStyleConfigurer {

  private final StyleConfiguration styleConfiguration;
  private boolean showGroupId;
  private boolean showArtifactId;
  private boolean showVersion;

  public DotGraphStyleConfigurer(StyleConfiguration styleConfiguration) {
    this.styleConfiguration = styleConfiguration;
  }

  @Override
  public void showGroupId() {
    this.showGroupId = true;
  }

  @Override
  public void showArtifactId() {
    this.showArtifactId = true;
  }

  @Override
  public void showVersion() {
    this.showVersion = true;
  }

  @Override
  public DotBuilder<GraphNode> configure(DotBuilder<GraphNode> graphBuilder) {
    DependencyNodeNameRenderer nodeNameRenderer = new DependencyNodeNameRenderer(true, false, false, this.styleConfiguration);
    DependencyEdgeRenderer edgeRenderer = new DependencyEdgeRenderer(this.showVersion, this.styleConfiguration);

    return graphBuilder
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer);
  }
}
