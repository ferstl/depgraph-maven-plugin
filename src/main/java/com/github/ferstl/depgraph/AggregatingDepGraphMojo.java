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
public class AggregatingDepGraphMojo extends AbstractDependencyGraphMojo {

  @Parameter(property = "includeParentProjects", defaultValue = "true")
  private boolean includeParentProjects;

  @Override
  protected GraphFactory createGraphFactory(GraphBuilderAdapter adapter, GraphBuilder graphBuilder, ArtifactFilter artifactFilter) {
      return new AggregatingGraphFactory(adapter, artifactFilter, graphBuilder, this.includeParentProjects);
  }
}
