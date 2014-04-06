package com.github.ferstl.depgraph;

import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.EdgeRenderer;
import com.github.ferstl.depgraph.dot.GraphBuilder;
import com.github.ferstl.depgraph.dot.Node;


class AggregatingGraphFactory implements GraphFactory {

  private final DependencyGraphBuilder dependencyGraphBuilder;
  private final ArtifactFilter artifactFilter;
  private final GraphBuilder graphBuilder;
  private final boolean includeParentProjects;

  public AggregatingGraphFactory(DependencyGraphBuilder dependencyGraphBuilder, ArtifactFilter artifactFilter, GraphBuilder graphBuilder, boolean includeParentProjects) {

    this.dependencyGraphBuilder = dependencyGraphBuilder;
    this.artifactFilter = artifactFilter;
    this.graphBuilder = graphBuilder;
    this.includeParentProjects = includeParentProjects;
  }

  @Override
  public String createGraph(MavenProject parent) {
    @SuppressWarnings("unchecked")
    List<MavenProject> collectedProjects = parent.getCollectedProjects();

    if (this.includeParentProjects) {
      buildModuleTree(parent, this.graphBuilder);
    }

    for (MavenProject collectedProject : collectedProjects) {
      // Process project only if its artifact is not filtered
      if (isPartOfGraph(collectedProject)) {
        DependencyNode root;
        try {
          root = this.dependencyGraphBuilder.buildDependencyGraph(collectedProject, this.artifactFilter);
        } catch (DependencyGraphBuilderException e) {
          throw new DependencyGraphException(e);
        }

        DotBuildingVisitor visitor = new DotBuildingVisitor(this.graphBuilder);
        root.accept(visitor);
      }
    }

    return this.graphBuilder.toString();
  }

  private void buildModuleTree(MavenProject parentProject, GraphBuilder graphBuilder) {
    @SuppressWarnings("unchecked")
    Collection<MavenProject> collectedProjects = parentProject.getCollectedProjects();
    for (MavenProject collectedProject : collectedProjects) {
      MavenProject child = collectedProject;
      MavenProject parent = collectedProject.getParent();

      while (parent != null) {
        Node parentNode = filterProject(parent);
        Node childNode = filterProject(child);

        graphBuilder.addEdge(parentNode, childNode, DottedEdgeRenderer.INSTANCE);

        // Stop if we reached the original parent project!
        if (parent.equals(parentProject)) {
          break;
        }

        child = parent;
        parent = parent.getParent();
      }
    }
  }

  private boolean isPartOfGraph(MavenProject project) {
    boolean result = this.artifactFilter.include(project.getArtifact());
    // Project is not filtered and is a parent project
    if (result && project.getModules().size() > 0) {
      result = result && this.includeParentProjects;
    }

    return result;
  }

  private Node filterProject(MavenProject project) {
    Artifact artifact = project.getArtifact();
    if (this.artifactFilter.include(artifact)) {
      return new DependencyNodeAdapter(artifact);
    }

    return null;
  }

  enum DottedEdgeRenderer implements EdgeRenderer {
    INSTANCE {

      @Override
      public String createEdgeAttributes(Node from, Node to) {
        return new AttributeBuilder().style("dotted").toString();
      }

    }
  }
}
