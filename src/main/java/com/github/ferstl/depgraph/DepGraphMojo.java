package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.GraphBuilder;
import com.github.ferstl.depgraph.dot.EdgeRenderer;
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
      GraphBuilder graphBuilder = new GraphBuilder()
          .useNodeRenderer(NodeRenderers.VERSIONLESS_ID)
          .useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL)
          .useEdgeRenderer(new DependencyEdgeRenderer(this.showVersions));

      return new SimpleGraphFactory(this.dependencyTreeBuilder, this.localRepository, artifactFilter, graphBuilder);
  }

  static enum EdgeStylers implements EdgeRenderer {
    INSTANCE;

    @Override
    public String createEdgeAttributes(Node from, Node to) {
      AttributeBuilder builder = new AttributeBuilder();

      switch(to.getResolution()) {
        case OMITTED_FOR_DUPLICATE:
          return " " + builder.style("dotted").toString();
        case OMMITTED_FOR_CONFLICT:
          return " " + builder.style("dashed").color("red").fontColor("red").label(to.getArtifact().getVersion()).toString();
        default:
          return " " + builder.label(to.getArtifact().getVersion());

      }
    }
  }
}
