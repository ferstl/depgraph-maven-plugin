package com.github.ferstl.depgraph.graph.gml;

import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class GmlGraphFormatter implements GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder result = new StringBuilder();
    result.append("graph [\n");

    //output nodes
    for (Node<?> node : nodes) {
      result.append("node [\n");
      result.append("id \"").append(node.getNodeId()).append("\"\n");
      if (isNotBlank(node.getNodeName())) {
        result.append("label \"").append(node.getNodeName()).append("\"\n");
      }
      result.append("]\n\n");
    }

    //output edges
    for (Edge edge : edges) {
      result.append("edge [\n");
      result.append("source \"").append(edge.getFromNodeId()).append("\"\n");
      result.append("target \"").append(edge.getToNodeId()).append("\"\n");
      if (isNotBlank(edge.getName())) {
        result.append("label \"").append(edge.getName()).append("\"\n");
      }
      result.append("]\n\n");
    }

    result.append("]");
    return result.toString();
  }
}
