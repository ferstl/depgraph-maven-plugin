package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.GraphBuilder;

@Mojo(
    name = "by-groupid",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class GraphByGroupIdMojo extends AbstractGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {

    GraphBuilder graphBuilder = new GraphBuilder()
        .useNodeRenderer(NodeRenderers.SCOPED_GROUP_ID)
        .useNodeLabelRenderer(NodeRenderers.GROUP_ID_LABEL);

    return new AggregatingDotGraphFactory(this.dependencyGraphBuilder, artifactFilter, graphBuilder, true);
  }
}
