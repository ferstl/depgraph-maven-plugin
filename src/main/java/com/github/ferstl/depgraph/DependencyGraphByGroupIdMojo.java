package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
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

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
    DotBuilder dotBuilder = new DotBuilder()
        .useNodeRenderer(NodeRenderers.SCOPED_GROUP_ID)
        .useNodeLabelRenderer(NodeRenderers.GROUP_ID_LABEL);

    GraphBuilderAdapter adapter = new GraphBuilderAdapter(this.dependencyGraphBuilder);
    return new SimpleGraphFactory(adapter, artifactFilter, dotBuilder);
  }

}
