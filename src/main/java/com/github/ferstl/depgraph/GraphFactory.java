package com.github.ferstl.depgraph;

import org.apache.maven.project.MavenProject;


interface GraphFactory {
  /**
   * Creates a graph for the given {@link MavenProject}.
   *
   * @return The String representation of the created graph.
   * @throws DependencyGraphException In case that the graph cannot be created.
   */
  String createGraph(MavenProject project);
}
