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

  public void addEdge(DependencyNode from, DependencyNode to) {
    String fromNode = this.nodeRenderer.render(from);
    String fromLabel = this.nodeLabelRenderer.render(from);
    String toNode = this.nodeRenderer.render(to);
    String toLabel = this.nodeLabelRenderer.render(to);

    this.nodeDefinitions.add(renderNode(fromNode, fromLabel));
    this.nodeDefinitions.add(renderNode(toNode, toLabel));

    this.edgeDefinitions.add(renderEdge(fromNode, toNode));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {").append("\n  node [shape=box]");
    for (String node : this.nodeDefinitions) {
      sb.append("\n  ").append(node);
    }

    for (String  edge : this.edgeDefinitions) {
      sb.append("\n  ").append(edge);
    }

    return sb.append("\n}").toString();
  }

  private String renderNode(String nodeName, String nodeLabel) {
    return "\"" + nodeName + "\"" + " [label=\"" + nodeLabel + "\"]";
  }

  private String renderEdge(String fromNode, String toNode) {
    return "\"" + fromNode + "\" -> \"" + toNode + "\"";
  }
}
