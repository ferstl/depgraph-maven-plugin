package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;

import com.github.ferstl.depgraph.dot.DotBuilder;


class SimpleGraphFactory implements GraphFactory {

  private final GraphBuilderAdapter graphBuilderAdapter;
  private final ArtifactFilter artifactFilter;
  private final DotBuilder dotBuilder;

  public SimpleGraphFactory(
      GraphBuilderAdapter graphBuilderAdapter, ArtifactFilter artifactFilter, DotBuilder dotBuilder) {

    this.graphBuilderAdapter = graphBuilderAdapter;
    this.artifactFilter = artifactFilter;
    this.dotBuilder = dotBuilder;
  }

  @Override
  public String createGraph(MavenProject project) {
    this.graphBuilderAdapter.buildDependencyGraph(project, this.artifactFilter, this.dotBuilder);
    return this.dotBuilder.toString();
  }

}
