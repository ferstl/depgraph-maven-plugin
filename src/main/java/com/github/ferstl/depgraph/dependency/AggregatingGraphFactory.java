/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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

import java.util.Collection;
import java.util.function.Supplier;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import com.github.ferstl.depgraph.graph.GraphBuilder;

/**
 * A graph factory that creates a dependency graph from a multi-module project. Child modules are treated as
 * dependencies of the parent project. The created graph is the <strong>union</strong> of the child modules' dependency
 * graphs.
 */
public class AggregatingGraphFactory implements GraphFactory {

  private final MavenGraphAdapter mavenGraphAdapter;
  private final Supplier<Collection<MavenProject>> subProjectSupplier;
  private final ArtifactFilter globalFilter;
  private final GraphBuilder<DependencyNode> graphBuilder;
  private final boolean includeParentProjects;
  private final boolean reduceEdges;

  public AggregatingGraphFactory(
      MavenGraphAdapter mavenGraphAdapter,
      Supplier<Collection<MavenProject>> subProjectSupplier,
      ArtifactFilter globalFilter,
      GraphBuilder<DependencyNode> graphBuilder,
      boolean includeParentProjects,
      boolean reduceEdges) {
    this.mavenGraphAdapter = mavenGraphAdapter;
    this.subProjectSupplier = subProjectSupplier;
    this.globalFilter = globalFilter;
    this.graphBuilder = graphBuilder;
    this.includeParentProjects = includeParentProjects;
    this.reduceEdges = reduceEdges;
  }

  @Override
  public String createGraph(MavenProject parent) {
    this.graphBuilder.graphName(parent.getArtifactId());

    if (this.includeParentProjects) {
      buildModuleTree(parent, this.graphBuilder);
    }

    Collection<MavenProject> collectedProjects = this.subProjectSupplier.get();
    for (MavenProject collectedProject : collectedProjects) {
      // Process project only if its artifact is not filtered
      if (isPartOfGraph(collectedProject)) {
        this.mavenGraphAdapter.buildDependencyGraph(collectedProject, this.globalFilter, this.graphBuilder);
      }
    }

    // Add the project as single node if the graph is empty
    Artifact artifact = parent.getArtifact();
    if (this.graphBuilder.isEmpty() && this.globalFilter.include(artifact)) {
      this.graphBuilder.addNode(new DependencyNode(artifact));
    }

    if (this.reduceEdges) {
      this.graphBuilder.reduceEdges();
    }

    return this.graphBuilder.toString();
  }

  private void buildModuleTree(MavenProject parentProject, GraphBuilder<DependencyNode> graphBuilder) {
    Collection<MavenProject> collectedProjects = parentProject.getCollectedProjects();
    for (MavenProject collectedProject : collectedProjects) {
      MavenProject child = collectedProject;
      MavenProject parent = collectedProject.getParent();

      while (parent != null) {
        DependencyNode parentNode = filterProject(parent);
        DependencyNode childNode = filterProject(child);

        graphBuilder.addEdge(parentNode, childNode);

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
    boolean isIncluded = this.globalFilter.include(project.getArtifact());
    // Project is not filtered and is a parent project
    if (isIncluded && project.getModules().size() > 0) {
      return this.includeParentProjects;
    }

    return isIncluded;
  }

  private DependencyNode filterProject(MavenProject project) {
    Artifact artifact = project.getArtifact();
    if (this.globalFilter.include(artifact)) {
      return new DependencyNode(artifact);
    }

    return null;
  }

}
