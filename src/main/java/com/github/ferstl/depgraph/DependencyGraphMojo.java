/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
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

import com.github.ferstl.depgraph.dot.DotBuilder;

@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = false,
    threadSafe = true)
public class DependencyGraphMojo extends AbstractGraphMojo {

  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  @Parameter(property = "showConflicts", defaultValue = "false")
  boolean showConflicts;

  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
    DotBuilder dotBuilder = createGraphBuilder();
    GraphBuilderAdapter adapter = createGraphBuilderAdapter();

    return new SimpleGraphFactory(adapter, artifactFilter, dotBuilder);
  }

  private DotBuilder createGraphBuilder() {
    DotBuilder dotBuilder = new DotBuilder().useNodeRenderer(NodeRenderers.VERSIONLESS_ID);
    dotBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL);

    if (requiresFullGraph() && this.showVersions) {
      // For the full graph we display the versions on the edges
      dotBuilder.useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions));

    } else if (this.showVersions) {
      // On the effective dependency graph we can display the versions within the nodes
      dotBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_VERSION_LABEL);
    }

    return dotBuilder;
  }

  private GraphBuilderAdapter createGraphBuilderAdapter() {
    GraphBuilderAdapter adapter;
    if (requiresFullGraph()) {
      adapter = new GraphBuilderAdapter(this.dependencyTreeBuilder, this.localRepository);
    } else {
      adapter = new GraphBuilderAdapter(this.dependencyGraphBuilder);
    }
    return adapter;
  }

  private boolean requiresFullGraph() {
    return this.showConflicts || this.showDuplicates;
  }
}
