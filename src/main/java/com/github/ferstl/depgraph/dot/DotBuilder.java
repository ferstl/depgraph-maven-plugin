package com.github.ferstl.depgraph.dot;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.ferstl.depgraph.dot.DotEscaper.escape;


public class DotBuilder {

  private final NodeRenderer nodeRenderer;
  private final NodeRenderer nodeLabelRenderer;
  private final EdgeStyler edgeStyler;
  private final Set<String> nodeDefinitions;
  private final Set<String> edgeDefinitions;

  public DotBuilder(NodeRenderer nodeRenderer, NodeRenderer nodeLabelRenderer) {
    this(nodeRenderer, nodeLabelRenderer, DefaultEdgeStyler.INSTANCE);
  }

  public DotBuilder(NodeRenderer nodeRenderer, NodeRenderer nodeLabelRenderer, EdgeStyler edgeStyler) {
    this.nodeLabelRenderer = nodeLabelRenderer;
    this.nodeRenderer = nodeRenderer;
    this.edgeStyler = edgeStyler;

    this.nodeDefinitions = new LinkedHashSet<>();
    this.edgeDefinitions = new LinkedHashSet<>();
  }

  // no edge will be created in case one or both nodes are null.
  public void addEdge(Node from, Node to) {
    if (from != null && to != null) {
      addNode(from);
      addNode(to);

      safelyAddEdge(from, to);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {")
    .append("\n  node ").append(new AttributeBuilder().shape("box").fontName("Helvetica"))
    .append("\n  edge ").append(new AttributeBuilder().fontName("Helvetica").fontSize(10));

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

  private void addNode(Node node) {
    String nodeName = this.nodeRenderer.render(node);
    String nodeLabel = this.nodeLabelRenderer.render(node);


    String nodeDefinition = escape(nodeName) + new AttributeBuilder().label(nodeLabel);;
    this.nodeDefinitions.add(nodeDefinition);
  }

  private void safelyAddEdge(Node fromNode, Node toNode) {
    String fromName = this.nodeRenderer.render(fromNode);
    String toName = this.nodeRenderer.render(toNode);

    String edgeDefinition = escape(fromName) + " -> " + escape(toName) + this.edgeStyler.styleEdge(fromNode, toNode);
    this.edgeDefinitions.add(edgeDefinition);
  }


  static enum DefaultEdgeStyler implements EdgeStyler {
    INSTANCE;

    @Override
    public String styleEdge(Node from, Node to) {
      return "";
    }

  }
}
