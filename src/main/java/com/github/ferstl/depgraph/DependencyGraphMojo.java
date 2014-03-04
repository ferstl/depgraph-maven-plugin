package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.GraphBuilder;

@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class DependencyGraphMojo extends AbstractDependencyGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
      GraphBuilder graphBuilder = new GraphBuilder()
          .useNodeRenderer(NodeRenderers.VERSIONLESS_ID)
          .useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL)
          .useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions));

      return new SimpleGraphFactory(this.dependencyTreeBuilder, this.localRepository, artifactFilter, graphBuilder);
  }
}
