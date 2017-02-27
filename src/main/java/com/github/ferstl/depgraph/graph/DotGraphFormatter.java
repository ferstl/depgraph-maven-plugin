package com.github.ferstl.depgraph.graph;


import java.util.Collection;

import static com.github.ferstl.depgraph.graph.DotEscaper.escape;

public class DotGraphFormatter implements GraphFormatter {

  private final DotAttributeBuilder nodeAttributeBuilder;
  private final DotAttributeBuilder edgeAttributeBuilder;

  public DotGraphFormatter() {
    this.nodeAttributeBuilder = new DotAttributeBuilder().shape("box").fontName("Helvetica");
    this.edgeAttributeBuilder = new DotAttributeBuilder().fontName("Helvetica").fontSize(10);
  }

  public DotGraphFormatter(DotAttributeBuilder nodeAttributeBuilder, DotAttributeBuilder edgeAttributeBuilder) {
    this.nodeAttributeBuilder = nodeAttributeBuilder;
    this.edgeAttributeBuilder = edgeAttributeBuilder;
  }

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder sb = new StringBuilder("digraph ").append(escape(graphName)).append(" {")
        .append("\n  node ").append(this.nodeAttributeBuilder)
        .append("\n  edge ").append(this.edgeAttributeBuilder);

    sb.append("\n\n  // Node Definitions:");
    for (Node<?> node : nodes) {
      String nodeId = node.getNodeId();
      String nodeName = node.getNodeName();
      sb.append("\n  ")
          .append(escape(nodeId))
          .append(nodeName);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (Edge edge : edges) {
      String edgeDefinition = escape(edge.getFromNodeId()) + " -> " + escape(edge.getToNodeId()) + edge.getName();
      sb.append("\n  ").append(edgeDefinition);
    }

    return sb.append("\n}").toString();
  }
}
