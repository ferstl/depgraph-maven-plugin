/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
package com.github.ferstl.depgraph.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.OrArtifactFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import static java.util.Collections.singletonList;

/**
 * Adapter for {@link DependencyGraphBuilder} and {@link DependencyTreeBuilder}.
 */
public final class MavenGraphAdapter {

  private final DependencyGraphBuilder dependencyGraphBuilder;
  private final DependencyTreeBuilder dependencyTreeBuilder;
  private final ArtifactRepository artifactRepository;
  private final ArtifactFilter transitiveIncludeExcludeFilter;
  private final ArtifactFilter targetFilter;
  private final boolean omitReachablePaths;
  private final Set<NodeResolution> includedResolutions;

  public MavenGraphAdapter(DependencyGraphBuilder builder, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, Set<NodeResolution> includedResolutions, boolean omitReachablePaths) {
    this.dependencyGraphBuilder = builder;
    this.transitiveIncludeExcludeFilter = transitiveIncludeExcludeFilter;
    this.targetFilter = targetFilter;
    this.omitReachablePaths = omitReachablePaths;
    this.includedResolutions = includedResolutions;
    this.dependencyTreeBuilder = null;
    this.artifactRepository = null;
  }

  public MavenGraphAdapter(DependencyTreeBuilder builder, ArtifactRepository artifactRepository, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, Set<NodeResolution> includedResolutions) {
    this.dependencyTreeBuilder = builder;
    this.artifactRepository = artifactRepository;
    this.transitiveIncludeExcludeFilter = transitiveIncludeExcludeFilter;
    this.targetFilter = targetFilter;
    this.omitReachablePaths = false;
    this.includedResolutions = includedResolutions;
    this.dependencyGraphBuilder = null;
  }

  public void buildDependencyGraph(MavenProject project, ArtifactFilter globalFilter, GraphBuilder<DependencyNode> graphBuilder) {
    ArtifactFilter transitiveDependencyFilter = createTransitiveDependencyFilter(project);
    if (this.dependencyGraphBuilder != null) {
      createGraph(project, globalFilter, transitiveDependencyFilter, graphBuilder);
    } else {
      createTree(project, globalFilter, transitiveDependencyFilter, graphBuilder);
    }
  }

  private void createGraph(MavenProject project, ArtifactFilter globalFilter, ArtifactFilter transitiveDependencyFilter, GraphBuilder<DependencyNode> graphBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.graph.DependencyNode root;
    try {
      root = this.dependencyGraphBuilder.buildDependencyGraph(project, globalFilter);
    } catch (DependencyGraphBuilderException e) {
      throw new DependencyGraphException(e);
    }

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(graphBuilder, transitiveDependencyFilter, this.targetFilter, this.omitReachablePaths);
    root.accept(visitor);
  }

  private void createTree(MavenProject project, ArtifactFilter globalFilter, ArtifactFilter transitiveDependencyFilter, GraphBuilder<DependencyNode> graphBuilder) throws DependencyGraphException {
    org.apache.maven.shared.dependency.tree.DependencyNode root;
    try {
      root = this.dependencyTreeBuilder.buildDependencyTree(project, this.artifactRepository, globalFilter);
    } catch (DependencyTreeBuilderException e) {
      throw new DependencyGraphException(e);
    }

    // Due to MNG-3236, we need to filter the artifacts on our own.
    GraphBuildingVisitor visitor = new GraphBuildingVisitor(graphBuilder, globalFilter, transitiveDependencyFilter, this.targetFilter, this.includedResolutions);
    root.accept(visitor);
  }

  private ArtifactFilter createTransitiveDependencyFilter(MavenProject project) {
    List<String> dependencyKeys = new ArrayList<>(project.getDependencies().size());
    for (Dependency dependency : project.getDependencies()) {
      dependencyKeys.add(dependency.getManagementKey());
    }

    // Matches direct dependencies or the configured transitive dependencies or the project itself
    OrArtifactFilter artifactFilter = new OrArtifactFilter();
    artifactFilter.add(this.transitiveIncludeExcludeFilter);
    artifactFilter.add(new StrictPatternIncludesArtifactFilter(dependencyKeys));
    artifactFilter.add(new StrictPatternIncludesArtifactFilter(singletonList(project.getArtifact().toString())));

    return artifactFilter;
  }
}
