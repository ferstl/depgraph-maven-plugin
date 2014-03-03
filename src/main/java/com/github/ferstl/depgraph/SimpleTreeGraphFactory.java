package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.GraphBuilder;


class SimpleTreeGraphFactory implements GraphFactory {

  private final DependencyGraphBuilder dependencyGraphBuilder;
  private final ArtifactFilter artifactFilter;
  private final GraphBuilder graphBuilder;

  public SimpleTreeGraphFactory(
      DependencyGraphBuilder dependencyGraphBuilder, ArtifactFilter artifactFilter, GraphBuilder graphBuilder) {

    this.dependencyGraphBuilder = dependencyGraphBuilder;
    this.artifactFilter = artifactFilter;
    this.graphBuilder = graphBuilder;
  }



  @Override
  public String createGraph(MavenProject project) throws DependencyGraphException {
    DependencyNode root;
    try {
      root = this.dependencyGraphBuilder.buildDependencyGraph(project, this.artifactFilter);
    } catch (DependencyGraphBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(this.graphBuilder);
    root.accept(visitor);

    return this.graphBuilder.toString();
  }

}
