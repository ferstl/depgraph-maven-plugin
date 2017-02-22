package com.github.ferstl.depgraph.graph;

import com.github.ferstl.depgraph.dot.DotBuilder;

public interface GraphStyleConfigurer {

  void showGroupId();

  void showArtifactId();

  void showVersion();

  DotBuilder<GraphNode> configure(DotBuilder<GraphNode> graphBuilder);
}
