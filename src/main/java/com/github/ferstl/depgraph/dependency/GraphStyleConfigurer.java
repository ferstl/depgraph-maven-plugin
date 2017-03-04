package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.GraphBuilder;

/**
 * API to configure the style of the dependency graph.
 */
public interface GraphStyleConfigurer {

  GraphStyleConfigurer showGroupIds(boolean showGroupId);

  GraphStyleConfigurer showArtifactIds(boolean showArtifactId);

  GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes);

  GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges);

  GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder);
}
