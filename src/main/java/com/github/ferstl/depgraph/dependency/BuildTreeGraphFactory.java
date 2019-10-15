package com.github.ferstl.depgraph.dependency;

import java.util.List;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.project.MavenProject;
import com.github.ferstl.depgraph.graph.GraphBuilder;

public class BuildTreeGraphFactory implements GraphFactory {

  private final GraphBuilder<DependencyNode> graphBuilder;
  private final ProjectDependencyGraph projectDependencyGraph;

  public BuildTreeGraphFactory(GraphBuilder<DependencyNode> graphBuilder, ProjectDependencyGraph projectDependencyGraph) {
    this.graphBuilder = graphBuilder;
    this.projectDependencyGraph = projectDependencyGraph;
  }

  @Override
  public String createGraph(MavenProject project) {
    for (MavenProject module : this.projectDependencyGraph.getSortedProjects()) {
      List<MavenProject> downstreamProjects = this.projectDependencyGraph.getDownstreamProjects(module, false);

      for (MavenProject downstreamProject : downstreamProjects) {
        DependencyNode from = new DependencyNode(module.getArtifact());
        DependencyNode to = new DependencyNode(downstreamProject.getArtifact());

        this.graphBuilder.removeNode(to);
        this.graphBuilder.addEdge(from, to);
      }
    }

    return this.graphBuilder.toString();
  }
}
