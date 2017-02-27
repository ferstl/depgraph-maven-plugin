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
package com.github.ferstl.depgraph.dependency;

import java.util.Collection;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import com.github.ferstl.depgraph.dot.DotBuilder;

/**
 * A graph factory that creates a dependency graph from a multi-module project. Child modules are treated as
 * dependencies of the parent project. The created graph is the <strong>union</strong> of the child modules' dependency
 * graphs.
 */
public class AggregatingGraphFactory implements GraphFactory {

  private final GraphBuilderAdapter graphBuilderAdapter;
  private final ArtifactFilter globalFilter;
  private final DotBuilder<DependencyNode> dotBuilder;
  private final boolean includeParentProjects;

  public AggregatingGraphFactory(GraphBuilderAdapter graphBuilderAdapter, ArtifactFilter globalFilter, DotBuilder<DependencyNode> dotBuilder, boolean includeParentProjects) {
    this.graphBuilderAdapter = graphBuilderAdapter;
    this.globalFilter = globalFilter;
    this.dotBuilder = dotBuilder;
    this.includeParentProjects = includeParentProjects;
  }

  @Override
  public String createGraph(MavenProject parent) {
    this.dotBuilder.graphName(parent.getArtifactId());

    if (this.includeParentProjects) {
      buildModuleTree(parent, this.dotBuilder);
    }

    List<MavenProject> collectedProjects = parent.getCollectedProjects();
    for (MavenProject collectedProject : collectedProjects) {
      // Process project only if its artifact is not filtered
      if (isPartOfGraph(collectedProject)) {
        this.graphBuilderAdapter.buildDependencyGraph(collectedProject, this.globalFilter, this.dotBuilder);
      }
    }

    return this.dotBuilder.toString();
  }

  private void buildModuleTree(MavenProject parentProject, DotBuilder<DependencyNode> dotBuilder) {
    Collection<MavenProject> collectedProjects = parentProject.getCollectedProjects();
    for (MavenProject collectedProject : collectedProjects) {
      MavenProject child = collectedProject;
      MavenProject parent = collectedProject.getParent();

      while (parent != null) {
        DependencyNode parentNode = filterProject(parent);
        DependencyNode childNode = filterProject(child);

        dotBuilder.addEdge(parentNode, childNode);

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
    boolean result = this.globalFilter.include(project.getArtifact());
    // Project is not filtered and is a parent project
    if (result && project.getModules().size() > 0) {
      result = result && this.includeParentProjects;
    }

    return result;
  }

  private DependencyNode filterProject(MavenProject project) {
    Artifact artifact = project.getArtifact();
    if (this.globalFilter.include(artifact)) {
      return new DependencyNode(artifact);
    }

    return null;
  }

}
