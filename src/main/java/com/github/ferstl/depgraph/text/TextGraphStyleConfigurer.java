package com.github.ferstl.depgraph.text;

import com.github.ferstl.depgraph.dependency.AbstractGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.text.TextGraphFormatter;

public class TextGraphStyleConfigurer extends AbstractGraphStyleConfigurer {

  boolean repeatTransitiveDependencies;

  @Override
  public GraphStyleConfigurer repeatTransitiveDependencies(boolean repeatTransitiveDependencies) {
    this.repeatTransitiveDependencies = repeatTransitiveDependencies;
    return this;
  }

  @Override
  public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
    return graphBuilder
        .useNodeNameRenderer(new TextDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showTypes, this.showClassifiers, this.showVersionsOnNodes))
        .useEdgeRenderer(new TextDependencyEdgeRenderer(this.showVersionOnEdges))
        .graphFormatter(new TextGraphFormatter(this.repeatTransitiveDependencies));
  }
}
