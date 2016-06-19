/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph.graph;

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
public final class GraphBuilderAdapter {

  private final DependencyGraphBuilder dependencyGraphBuilder;
  private final DependencyTreeBuilder dependencyTreeBuilder;
  private final ArtifactRepository artifactRepository;
  private final ArtifactFilter targetFilter;

  public GraphBuilderAdapter(DependencyGraphBuilder builder, ArtifactFilter targetFilter) {
    this.dependencyGraphBuilder = builder;
    this.targetFilter = targetFilter;
    this.dependencyTreeBuilder = null;
    this.artifactRepository = null;
  }

  public GraphBuilderAdapter(DependencyTreeBuilder builder, ArtifactRepository artifactRepository, ArtifactFilter targetFilter) {
    this.dependencyGraphBuilder = null;
    this.dependencyTreeBuilder = builder;
    this.artifactRepository = artifactRepository;
    this.targetFilter = targetFilter;
  }

  public void buildDependencyGraph(MavenProject project, ArtifactFilter globalFilter, DotBuilder<GraphNode> dotBuilder) {

    if (this.dependencyGraphBuilder != null) {
      createGraph(project, globalFilter, dotBuilder);
    } else {
      createTree(project, globalFilter, dotBuilder);
    }
  }

  private void createGraph(MavenProject project, ArtifactFilter globalFilter, DotBuilder<GraphNode> dotBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.graph.DependencyNode root;
    try {
      root = this.dependencyGraphBuilder.buildDependencyGraph(project, globalFilter);
    } catch (DependencyGraphBuilderException e) {
      throw new DependencyGraphException(e);
    }

    DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder, this.targetFilter);
    root.accept(visitor);
  }

  private void createTree(MavenProject project, ArtifactFilter globalFilter, DotBuilder<GraphNode> dotBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.tree.DependencyNode root;
    try {
      root = this.dependencyTreeBuilder.buildDependencyTree(project, this.artifactRepository, globalFilter);
    } catch (DependencyTreeBuilderException e) {
      throw new DependencyGraphException(e);
    }

    // Due to MNG-3236, we need to filter the artifacts on our own.
    DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder, globalFilter, this.targetFilter);
    root.accept(visitor);
  }
}
