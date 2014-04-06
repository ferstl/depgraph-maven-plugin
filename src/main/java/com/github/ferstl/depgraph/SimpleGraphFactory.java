package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;

import com.github.ferstl.depgraph.dot.GraphBuilder;


class SimpleGraphFactory implements GraphFactory {

  private final GraphBuilderAdapter graphBuilderAdapter;
  private final ArtifactFilter artifactFilter;
  private final GraphBuilder graphBuilder;

  public SimpleGraphFactory(
      GraphBuilderAdapter graphBuilderAdapter, ArtifactFilter artifactFilter, GraphBuilder graphBuilder) {

    this.graphBuilderAdapter = graphBuilderAdapter;
    this.artifactFilter = artifactFilter;
    this.graphBuilder = graphBuilder;
  }

  @Override
  public String createGraph(MavenProject project) {
    this.graphBuilderAdapter.buildDependencyGraph(project, this.artifactFilter, this.graphBuilder);
    return this.graphBuilder.toString();
  }

}
