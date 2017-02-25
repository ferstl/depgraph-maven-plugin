package com.github.ferstl.depgraph.graph;

import com.github.ferstl.depgraph.dot.DotBuilder;

/**
 * API to configure the style of the dependency graph.
 */
public interface GraphStyleConfigurer {

  GraphStyleConfigurer showGroupIds(boolean showGroupId);

  GraphStyleConfigurer showArtifactIds(boolean showArtifactId);

  GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes);

  GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges);

  DotBuilder<GraphNode> configure(DotBuilder<GraphNode> graphBuilder);
}
