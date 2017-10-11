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
package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.AggregatingGraphFactory;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.MavenGraphAdapter;
import com.github.ferstl.depgraph.graph.GraphBuilder;

/**
 * Aggregates all dependencies of a multi-module project into one single graph.
 */
@Mojo(
    name = "aggregate",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    threadSafe = true)
public class AggregatingDependencyGraphMojo extends AbstractAggregatingGraphMojo {

  /**
   * If set to {@code true}, the created graph will show the {@code groupId} on all artifacts.
   *
   * @since 1.0.3
   */
  @Parameter(property = "showGroupIds", defaultValue = "false")
  boolean showGroupIds;

  /**
   * If set to {@code true} the artifact nodes will show version information.
   *
   * @since 1.0.0
   */
  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  /**
   * If set to {@code true}, the created graph will show type information an all artifacts.
   *
   * @since 2.3.0
   */
  @Parameter(property = "showTypes", defaultValue = "false")
  boolean showTypes;

  /**
   * If set to {@code true}, the created graph will show classifier information an all artifacts.
   *
   * @since 2.3.0
   */
  @Parameter(property = "showClassifiers", defaultValue = "false")
  boolean showClassifiers;

  /**
   * If set to {@code true}, all parent modules (&lt;packaging&gt;pom&lt;/packaging&gt) will be shown with a dotted
   * arrow pointing to their child modules.
   *
   * @since 1.0.0
   */
  @Parameter(property = "includeParentProjects", defaultValue = "false")
  private boolean includeParentProjects;

  /**
   * Merge dependencies with multiple types into one graph node instead of having a node per type.
   *
   * @since 2.3.0
   */
  @Parameter(property = "mergeTypes", defaultValue = "false")
  private boolean mergeTypes;

  /**
   * Merge dependencies with multiple classifiers into one graph node instead of having a node per classifier.
   *
   * @since 2.3.0
   */
  @Parameter(property = "mergeClassifiers", defaultValue = "false")
  boolean mergeClassifiers;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {
    handleOptionsForFullGraph();

    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId()
        .withClassifier(!this.mergeClassifiers)
        .withType(!this.mergeTypes)
        .withScope(!this.mergeScopes);

    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
        .showGroupIds(this.showGroupIds)
        .showArtifactIds(true)
        .showTypes(this.showTypes)
        .showClassifiers(this.showClassifiers)
        .showVersionsOnNodes(this.showVersions)
        // This graph won't show any conflicting dependencies. So don't show versions on edges
        .showVersionsOnEdges(false)
        .repeatTransitiveDependencies(this.repeatTransitiveDependenciesInTextGraph)
        .configure(GraphBuilder.create(nodeIdRenderer));

    MavenGraphAdapter adapter = new MavenGraphAdapter(this.dependencyGraphBuilder, transitiveFilter, targetFilter, this.reduceEdges);
    return new AggregatingGraphFactory(adapter, createReactorOrderSubProjectSupplier(), globalFilter, graphBuilder, this.includeParentProjects);
  }

  private void handleOptionsForFullGraph() {
    if (showFullGraph()) {
      this.showGroupIds = true;
      this.showVersions = true;
      this.showTypes = true;
      this.showClassifiers = true;
    }
  }
}
