package com.github.ferstl.depgraph;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

import com.github.ferstl.depgraph.dot.GraphBuilder;


class SimpleTreeGraphFactory implements GraphFactory {

  private final DependencyTreeBuilder dependencyTreeBuilder;
  private final ArtifactRepository artifactRepository;
  private final ArtifactFilter artifactFilter;
  private final GraphBuilder graphBuilder;


  public SimpleTreeGraphFactory(
      DependencyTreeBuilder dependencyTreeBuilder,
      ArtifactRepository artifactRepository,
      ArtifactFilter artifactFilter,
      GraphBuilder graphBuilder) {

    this.dependencyTreeBuilder = dependencyTreeBuilder;
    this.artifactRepository = artifactRepository;
    this.artifactFilter = artifactFilter;
    this.graphBuilder = graphBuilder;
  }



  @Override
  public String createGraph(MavenProject project) {
    DependencyNode root;
    try {
      root = this.dependencyTreeBuilder.buildDependencyTree(project, this.artifactRepository, this.artifactFilter);
    } catch (DependencyTreeBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(this.graphBuilder);
    root.accept(visitor);

    return this.graphBuilder.toString();
  }
}
