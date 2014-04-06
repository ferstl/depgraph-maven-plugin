package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.ferstl.depgraph.dot.GraphBuilder;


abstract class AbstractDependencyGraphMojo extends AbstractGraphMojo {

  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  @Parameter(property = "showConicts", defaultValue = "false")
  boolean showConflicts;

  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  @Override
  protected final GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
    GraphBuilder graphBuilder = createGraphBuilder();
    GraphBuilderAdapter adapter = createGraphBuilderAdapter();

    return createGraphFactory(adapter, graphBuilder, artifactFilter);
  }

  protected abstract GraphFactory createGraphFactory(GraphBuilderAdapter adapter, GraphBuilder graphBuilder, ArtifactFilter artifactFilter);

  protected final boolean requiresFullGraph() {
    return this.showConflicts || this.showDuplicates;
  }

  protected final GraphBuilder createGraphBuilder() {
    GraphBuilder graphBuilder = new GraphBuilder().useNodeRenderer(NodeRenderers.VERSIONLESS_ID);

    if (requiresFullGraph() && this.showVersions) {
      // For the full graph we display the versions on the edges
      graphBuilder.useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions));

    } else if (this.showVersions) {
      // On the effective dependency graph we can display the versions within the nodes
      graphBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_VERSION_LABEL);

    } else {
      graphBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL);
    }

    return graphBuilder;
  }

  protected final GraphBuilderAdapter createGraphBuilderAdapter() {
    GraphBuilderAdapter adapter;
    if (requiresFullGraph()) {
      adapter = new GraphBuilderAdapter(this.dependencyTreeBuilder, this.localRepository);
    } else {
      adapter = new GraphBuilderAdapter(this.dependencyGraphBuilder);
    }
    return adapter;
  }
}
