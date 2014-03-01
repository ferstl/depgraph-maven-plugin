package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.dot.EdgeStyler;
import com.github.ferstl.depgraph.dot.Node;

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
      DotBuilder dotBuilder = new DotBuilder(NodeRenderers.VERSIONLESS_ID, NodeRenderers.ARTIFACT_ID_LABEL, EdgeStylers.INSTANCE);
      return new SimpleGraphFactory(this.dependencyTreeBuilder, this.localRepository, artifactFilter, dotBuilder);
  }

  static enum EdgeStylers implements EdgeStyler {
    INSTANCE;

    @Override
    public String styleEdge(Node from, Node to) {
      if (to.getResolution() != NodeResolution.INCLUDED) {
        return "[style=dotted]";
      }

      return "";
    }
  }
}
