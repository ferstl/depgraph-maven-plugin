package com.github.ferstl.depgraph.dot;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.shared.dependency.graph.DependencyNode;


public class DotBuilder {

  private final NodeRenderer nodeRenderer;
  private final NodeRenderer nodeLabelRenderer;
  private final Set<String> nodeDefinitions;
  private final Set<String> edgeDefinitions;

  public DotBuilder(NodeRenderer nodeRenderer, NodeRenderer nodeLabelRenderer) {
    this.nodeLabelRenderer = nodeLabelRenderer;
    this.nodeRenderer = nodeRenderer;

    this.nodeDefinitions = new LinkedHashSet<>();
    this.edgeDefinitions = new LinkedHashSet<>();
  }

  // no edge will be created in case one or both nodes are null.
  public void addEdge(DependencyNode from, DependencyNode to) {
    String fromNode = safeRenderNode(from, this.nodeRenderer);
    String fromLabel = safeRenderNode(from, this.nodeLabelRenderer);
    String toNode = safeRenderNode(to, this.nodeRenderer);
    String toLabel = safeRenderNode(to, this.nodeLabelRenderer);

    safeAddNode(fromNode, fromLabel);
    safeAddNode(toNode, toLabel);

    safeAddEdge(fromNode, toNode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {").append("\n  node [shape=box, fontname=\"Helvetica\"]");

    sb.append("\n\n  // Node Definitions:");
    for (String node : this.nodeDefinitions) {
      sb.append("\n  ").append(node);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (String  edge : this.edgeDefinitions) {
      sb.append("\n  ").append(edge);
    }

    return sb.append("\n}").toString();
  }

  private String safeRenderNode(DependencyNode node, NodeRenderer renderer) {
    if (node != null) {
      return renderer.render(node);
    }

    return null;
  }

  private void safeAddNode(String nodeName, String nodeLabel) {
    if (nodeName != null && nodeLabel != null) {
      String nodeDefinition = "\"" + nodeName + "\"" + " [label=\"" + nodeLabel + "\"]";
      this.nodeDefinitions.add(nodeDefinition);
    }
  }

  private void safeAddEdge(String fromNode, String toNode) {
    if (fromNode != null && toNode != null) {
      String edgeDefinition = "\"" + fromNode + "\" -> \"" + toNode + "\"";
      this.edgeDefinitions.add(edgeDefinition);
    }
  }
}
