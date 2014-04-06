package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.GraphBuilder;

@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
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
    GraphBuilder graphBuilder = createGraphBuilder();

    if (requiresFullGraph()) {
      return new SimpleTreeGraphFactory(this.dependencyGraphBuilder, artifactFilter, graphBuilder);
    }

    return new SimpleGraphFactory(this.dependencyTreeBuilder, this.localRepository, artifactFilter, graphBuilder);
  }

  protected boolean requiresFullGraph() {
    return this.showConflicts || this.showDuplicates;
  }

  private GraphBuilder createGraphBuilder() {
    GraphBuilder graphBuilder = new GraphBuilder().useNodeRenderer(NodeRenderers.VERSIONLESS_ID);

    if (requiresFullGraph() && this.showVersions) {
      graphBuilder.useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions));

    } else if(this.showVersions) {
      graphBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_VERSION_LABEL);

    } else {
      graphBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL);
    }

    return graphBuilder;
  }

}
