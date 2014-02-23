package com.github.ferstl.depgraph;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

import com.github.ferstl.depgraph.dot.DotBuilder;


class SimpleGraphFactory implements GraphFactory {

  private final DependencyTreeBuilder dependencyTreeBuilder;
  private final ArtifactRepository artifactRepository;
  private final ArtifactFilter artifactFilter;
  private final DotBuilder dotBuilder;


  public SimpleGraphFactory(
      DependencyTreeBuilder dependencyTreeBuilder,
      ArtifactRepository artifactRepository,
      ArtifactFilter artifactFilter,
      DotBuilder dotBuilder) {

    this.dependencyTreeBuilder = dependencyTreeBuilder;
    this.artifactRepository = artifactRepository;
    this.artifactFilter = artifactFilter;
    this.dotBuilder = dotBuilder;
  }



  @Override
  public String createGraph(MavenProject project) throws DependencyGraphException {
    DependencyNode root;
    try {
      root = this.dependencyTreeBuilder.buildDependencyTree(project, this.artifactRepository, this.artifactFilter);
    } catch (DependencyTreeBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(this.dotBuilder);
    root.accept(visitor);

    return this.dotBuilder.toString();
  }
}
