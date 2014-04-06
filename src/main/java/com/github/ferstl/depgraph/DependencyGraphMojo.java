package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.GraphBuilder;

@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class DependencyGraphMojo extends AbstractDependencyGraphMojo {

  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  @Parameter(property = "showConflicts", defaultValue = "false")
  boolean showConflicts;

  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  protected GraphFactory createGraphFactory(GraphBuilderAdapter adapter, GraphBuilder graphBuilder, ArtifactFilter artifactFilter) {
    return new SimpleGraphFactory(adapter, artifactFilter, graphBuilder);
  }
}
