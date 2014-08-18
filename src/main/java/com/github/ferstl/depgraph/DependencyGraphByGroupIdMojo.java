package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dot.DotBuilder;

/**
 * Creates a graph containing the group IDs of all dependencies.
 */
@Mojo(
    name = "by-groupid",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = false,
    threadSafe = true)
public class DependencyGraphByGroupIdMojo extends AbstractGraphMojo {

  /**
   * If set to {@code true}, the graph will additionally contain conflicting dependencies. Note that the dependency
   * graph may not be 100% accurate when this flag is enabled and the plugin is executed with a Maven version greater or
   * equal 3.0!
   *
   * @since 1.0.0
   */
  @Parameter(property = "showConflicts", defaultValue = "false")
  boolean showConflicts;

  /**
   * If set to {@code true}, the graph will additionally contain duplicate dependencies. Note that the dependency graph
   * may not be 100% accurate when this flag is enabled and the plugin is executed with a Maven version greater or equal
   * 3.0!
   *
   * @since 1.0.0
   */
  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
    DotBuilder dotBuilder = createGraphBuilder();

    GraphBuilderAdapter adapter = createGraphBuilderAdapter();
    return new SimpleGraphFactory(adapter, artifactFilter, dotBuilder);
  }

  private DotBuilder createGraphBuilder() {
    DotBuilder dotBuilder = new DotBuilder()
        .useNodeRenderer(NodeRenderers.SCOPED_GROUP_ID)
        .useNodeLabelRenderer(NodeRenderers.GROUP_ID_LABEL)
        .omitSelfReferences();

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
