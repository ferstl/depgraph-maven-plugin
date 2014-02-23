package com.github.ferstl.depgraph;

import org.apache.maven.project.MavenProject;


interface GraphFactory {
  String createGraph(MavenProject project) throws DependencyGraphException;
}
