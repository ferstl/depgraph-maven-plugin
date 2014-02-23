package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.DotBuilder;

@Mojo(
    name = "graph",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class DepGraphMojo extends AbstractDepGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {

      DotBuilder dotBuilder = new DotBuilder(NodeRenderers.VERSIONLESS_ID, NodeRenderers.ARTIFACT_ID_LABEL);
      return new SimpleDotGraphFactory(this.dependencyGraphBuilder, artifactFilter, dotBuilder);
  }

}
