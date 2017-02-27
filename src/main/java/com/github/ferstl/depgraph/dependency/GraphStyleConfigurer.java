package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.DotBuilder;

/**
 * API to configure the style of the dependency graph.
 */
public interface GraphStyleConfigurer {

  GraphStyleConfigurer showGroupIds(boolean showGroupId);

  GraphStyleConfigurer showArtifactIds(boolean showArtifactId);

  GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes);

  GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges);

  DotBuilder<DependencyNode> configure(DotBuilder<DependencyNode> graphBuilder);
}
