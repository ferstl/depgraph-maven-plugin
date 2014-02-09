package com.github.ferstl.depgraph;

import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.DotBuilder;

@Mojo(
    name = "graph",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class DepGraphMojo extends AbstractMojo {

  @Parameter(
      alias = "gropIdClusters",
      property = "groupIdClusters",
      readonly = true,
      required = false)
  private List<String> groupIdClusters;

  @Component
  private MavenProject project;

  @Component( hint = "default" )
  private DependencyGraphBuilder dependencyGraphBuilder;

  @Override
  public void execute() throws MojoExecutionException {
    try {
      DotBuilder dotBuilder = new DotBuilder(ArtifactIdRenderer.VERSIONLESS_ID, ArtifactIdRenderer.ARTIFACT_ID);

      @SuppressWarnings("unchecked")
      List<MavenProject> collectedProjects = this.project.getCollectedProjects();
      buildModuleTree(collectedProjects, dotBuilder);

      for (MavenProject collectedProject : collectedProjects) {
        DependencyNode root = this.dependencyGraphBuilder.buildDependencyGraph(collectedProject, null);

        DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder);
        root.accept(visitor);
      }

      System.err.println(dotBuilder);

    } catch (DependencyGraphBuilderException e) {
      throw new MojoExecutionException("boom");
    }
  }

  public void buildModuleTree(Collection<MavenProject> collectedProjects, DotBuilder dotBuilder) {
    System.err.println("Project: " + this.project + ", Parent: " + this.project.getParent());
    for (MavenProject collectedProject : collectedProjects) {
      MavenProject child = collectedProject;
      MavenProject parent = collectedProject.getParent();

      while (parent != null) {
        ArtifactNode parentNode = new ArtifactNode(parent.getArtifact());
        ArtifactNode childNode = new ArtifactNode(child.getArtifact());
        dotBuilder.addEdge(parentNode, childNode);

        child = parent;
        parent = parent.getParent();
      }
    }
  }
}
