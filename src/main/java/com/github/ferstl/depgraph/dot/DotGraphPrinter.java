package com.github.ferstl.depgraph.dot;


import java.util.Collection;

import static com.github.ferstl.depgraph.dot.DotEscaper.escape;

public class DotGraphPrinter {

  private final AttributeBuilder nodeAttributeBuilder;
  private final AttributeBuilder edgeAttributeBuilder;

  public DotGraphPrinter() {
    this.nodeAttributeBuilder = new AttributeBuilder().shape("box").fontName("Helvetica");
    this.edgeAttributeBuilder = new AttributeBuilder().fontName("Helvetica").fontSize(10);
  }

  public DotGraphPrinter(AttributeBuilder nodeAttributeBuilder, AttributeBuilder edgeAttributeBuilder) {
    this.nodeAttributeBuilder = nodeAttributeBuilder;
    this.edgeAttributeBuilder = edgeAttributeBuilder;
  }

  public String printGraph(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
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
