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
package com.github.ferstl.depgraph;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.graph.DependencyEdgeRenderer;
import com.github.ferstl.depgraph.graph.DependencyNodeNameRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilderAdapter;
import com.github.ferstl.depgraph.graph.GraphFactory;
import com.github.ferstl.depgraph.graph.GraphNode;
import com.github.ferstl.depgraph.graph.NodeIdRenderers;
import com.github.ferstl.depgraph.graph.NodeResolution;
import com.github.ferstl.depgraph.graph.SimpleGraphFactory;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.EnumSet;

import static java.util.EnumSet.allOf;
import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;

/**
 * Creates a dependency graph of a maven module.
 */
@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = false,
    threadSafe = true)
public class DependencyGraphMojo extends AbstractGraphMojo {

  /**
   * If set to {@code true}, the created graph will show the {@code groupId} on all artifacts.
   *
   * @since 1.0.3
   */
  @Parameter(property = "showGroupIds", defaultValue = "false")
  boolean showGroupIds;

  /**
   * If set to {@code true}, the created graph will show version information an all artifacts. Depending on the flags
   * {@link #showDuplicates} and {@link #showConflicts}, the version will either be shown directly in the artifact
   * nodes or on the dependency edges.
   *
   * @since 1.0.0
   */
  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  /**
   * If set to {@code true}, the graph will additionally contain conflicting dependencies. Note that the dependency
   * graph may not be 100% accurate when this flag is enabled and the plugin is executed with a Maven version greater
   * or equal 3.0!
   *
   * @since 1.0.0
   */
  @Parameter(property = "showConflicts", defaultValue = "false")
  boolean showConflicts;

  /**
   * If set to {@code true}, the graph will additionally contain duplicate dependencies. Note that the dependency graph
   * may not be 100% accurate when this flag is enabled and the plugin is executed with a Maven version greater or
   * equal 3.0!
   *
   * @since 1.0.0
   */
  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter targetFilter, StyleConfiguration styleConfiguration) {
    DotBuilder<GraphNode> dotBuilder = createDotBuilder(styleConfiguration);
    GraphBuilderAdapter adapter = createGraphBuilderAdapter(targetFilter);

    return new SimpleGraphFactory(adapter, globalFilter, dotBuilder);
  }

  DotBuilder<GraphNode> createDotBuilder(StyleConfiguration styleConfiguration) {
    DotBuilder<GraphNode> dotBuilder = new DotBuilder<GraphNode>()
        .nodeStyle(styleConfiguration.defaultNodeAttributes())
        .edgeStyle(styleConfiguration.defaultEdgeAttributes())
        .useNodeIdRenderer(NodeIdRenderers.VERSIONLESS_ID);

    boolean fullGraph = requiresFullGraph();
    if (fullGraph) {
      dotBuilder.useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions, styleConfiguration));
    }

    dotBuilder.useNodeNameRenderer(new DependencyNodeNameRenderer(this.showGroupIds, true, this.showVersions, styleConfiguration));

    return dotBuilder;
  }

  private GraphBuilderAdapter createGraphBuilderAdapter(ArtifactFilter targetFilter) {
    GraphBuilderAdapter adapter;
    if (requiresFullGraph()) {
      EnumSet<NodeResolution> resolutions = allOf(NodeResolution.class);
      resolutions = !this.showConflicts ? complementOf(of(NodeResolution.OMITTED_FOR_CONFLICT)) : resolutions;
      resolutions = !this.showDuplicates ? complementOf(of(NodeResolution.OMITTED_FOR_DUPLICATE)) : resolutions;

      adapter = new GraphBuilderAdapter(this.dependencyTreeBuilder, this.localRepository, targetFilter, resolutions);
    } else {
      adapter = new GraphBuilderAdapter(this.dependencyGraphBuilder, targetFilter);
    }
    return adapter;
  }

  private boolean requiresFullGraph() {
    return this.showConflicts || this.showDuplicates;
  }
}
