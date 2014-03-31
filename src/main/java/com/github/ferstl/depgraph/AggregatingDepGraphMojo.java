package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.GraphBuilder;

@Mojo(
    name = "aggregate",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class AggregatingDepGraphMojo extends AbstractGraphMojo {

  @Parameter(property = "includeParentProjects", defaultValue = "true")
  private boolean includeParentProjects;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {

      GraphBuilder graphBuilder = new GraphBuilder()
        .useNodeRenderer(NodeRenderers.VERSIONLESS_ID)
        .useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL);

      return new AggregatingDotGraphFactory(this.dependencyGraphBuilder, artifactFilter, graphBuilder, this.includeParentProjects);
  }
}
