/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
package com.github.ferstl.depgraph;

import java.util.Collection;
import java.util.function.Supplier;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractAggregatingDependencyGraphMojo extends AbstractDependencyGraphMojo {

  /**
   * Merge dependencies that occur in multiple scopes into one graph node instead of having a node per scope.
   *
   * @since 2.0.0
   */
  @Parameter(property = "mergeScopes", defaultValue = "false")
  boolean mergeScopes;

  /**
   * Omit all edges that are already reachable via a different path in the dependency graph. This will prefer dependencies
   * of modules that are higher in the reactor build order and thus reflect the architecture of the application better.
   *
   * @since 3.0.0
   */
  @Parameter(property = "reduceEdges", defaultValue = "true")
  boolean reduceEdges;

  /**
   * If set to {@code true}, this option will repeat all transitive dependencies in the text graph.<br/>
   * <strong>Example:</strong><br/>
   * Suppose a dependency graph with one child node containing a transitive dependency and a second child node with a
   * dependency to the first child node.</br>
   * When this option is disabled, the graph will look like this:
   * <pre>
   *   root
   *   +- child-1
   *   |  \- child-1.1
   *   +- child-2
   *      \- child-1
   * </pre>
   * When this option is enabled, the graph will show the transitive dependencies of child-1 on child-2 as well:
   * <pre>
   *   root
   *   +- child-1
   *   |  \- child-1.1
   *   +- child-2
   *      \- child-1
   *         \- child-1.1
   * </pre>
   *
   * @since 3.0.0
   */
  @Parameter(property = "repeatTransitiveDependenciesInTextGraph", defaultValue = "false")
  boolean repeatTransitiveDependenciesInTextGraph;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  Supplier<Collection<MavenProject>> subProjectsInReactorOrder() {
    return () -> AbstractAggregatingDependencyGraphMojo.this.mavenSession.getProjectDependencyGraph().getSortedProjects();
  }
}
