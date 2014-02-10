package com.github.ferstl.depgraph;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;

import com.github.ferstl.depgraph.dot.DotBuilder;


interface DotGraphCreator {
  String createDotGraph(MavenProject project, DotBuilder dotBuilder) throws DependencyGraphBuilderException;
}
