package com.github.ferstl.depgraph;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

import com.github.ferstl.depgraph.dot.DotBuilder;

/**
 * Adapter for {@link DependencyGraphBuilder} and {@link DependencyTreeBuilder}.
 */
final class GraphBuilderAdapter {
  private DependencyGraphBuilder dependencyGraphBuilder;
  private DependencyTreeBuilder dependencyTreeBuilder;
  private ArtifactRepository artifactRepository;

  public GraphBuilderAdapter(DependencyGraphBuilder builder) {
    this.dependencyGraphBuilder = builder;
  }

  public GraphBuilderAdapter(DependencyTreeBuilder builder, ArtifactRepository artifactRepository) {
    this.dependencyTreeBuilder = builder;
    this.artifactRepository = artifactRepository;
  }

  public void buildDependencyGraph(MavenProject project, ArtifactFilter artifactFilter, DotBuilder dotBuilder) {

    if (this.dependencyGraphBuilder != null) {
      createGraph(project, artifactFilter, dotBuilder);
    } else {
      createTree(project, artifactFilter, dotBuilder);
    }
  }

  private void createGraph(MavenProject project, ArtifactFilter artifactFilter, DotBuilder dotBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.graph.DependencyNode root;
    try {
      root = this.dependencyGraphBuilder.buildDependencyGraph(project, artifactFilter);
    } catch (DependencyGraphBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder);
    root.accept(visitor);
  }

  private void createTree(MavenProject project, ArtifactFilter artifactFilter, DotBuilder dotBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.tree.DependencyNode root;
    try {
      root = this.dependencyTreeBuilder.buildDependencyTree(project, this.artifactRepository, artifactFilter);
    } catch (DependencyTreeBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder);
    root.accept(visitor);
  }
}
