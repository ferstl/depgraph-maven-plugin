package com.github.ferstl.depgraph.graph;

import java.util.Collection;

public class GmlGraphFormatter implements GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder result = new StringBuilder();
    result.append("graph [\n");

    //output nodes
    for (Node<?> node : nodes) {
      result.append("node [\n");
      result.append("id \"").append(node.getNodeId()).append("\"\n");
      result.append("label \"").append(node.getNodeName()).append("\"\n");
      result.append("]\n\n");
    }

    //output edges
    for (Edge edge : edges) {
      result.append("edge [\n");
      result.append("source \"").append(edge.getFromNodeId()).append("\"\n");
      result.append("target \"").append(edge.getToNodeId()).append("\"\n");
      result.append("]\n\n");
    }

    result.append("]");
    return result.toString();
  }
}
