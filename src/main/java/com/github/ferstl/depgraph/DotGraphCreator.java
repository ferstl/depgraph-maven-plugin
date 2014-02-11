package com.github.ferstl.depgraph;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;


interface DotGraphCreator {
  String createDotGraph(MavenProject project) throws DependencyGraphBuilderException;
}
