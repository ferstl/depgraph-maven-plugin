package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.text.TextGraphFormatter;

public class TextGraphStyleConfigurer extends AbstractGraphStyleConfigurer {

  @Override
  public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
    return graphBuilder
        .useNodeNameRenderer(new TextDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showTypes, this.showClassifiers, this.showVersionsOnNodes))
        .useEdgeRenderer(new TextDependencyEdgeRenderer(this.showVersionOnEdges))
        .graphFormatter(new TextGraphFormatter());
  }
}
