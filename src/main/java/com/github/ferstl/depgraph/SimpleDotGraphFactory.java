package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.DotBuilder;


public class SimpleDotGraphFactory implements GraphFactory {

  private final DependencyGraphBuilder dependencyGraphBuilder;
  private final ArtifactFilter artifactFilter;
  private final DotBuilder dotBuilder;

  public SimpleDotGraphFactory(
      DependencyGraphBuilder dependencyGraphBuilder, ArtifactFilter artifactFilter, DotBuilder dotBuilder) {

    this.dependencyGraphBuilder = dependencyGraphBuilder;
    this.artifactFilter = artifactFilter;
    this.dotBuilder = dotBuilder;
  }



  @Override
  public String createGraph(MavenProject project) throws DependencyGraphBuilderException {
    DependencyNode root = this.dependencyGraphBuilder.buildDependencyGraph(project, this.artifactFilter);

    DotBuildingVisitor visitor = new DotBuildingVisitor(this.dotBuilder);
    root.accept(visitor);

    return this.dotBuilder.toString();
  }


}
