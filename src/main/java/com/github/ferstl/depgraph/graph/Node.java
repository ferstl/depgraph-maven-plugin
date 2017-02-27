package com.github.ferstl.depgraph.graph;

public final class Node<T> {

  private final String nodeId;
  private final String nodeName;
  final T nodeObject;

  public Node(String nodeId, String nodeName, T nodeObject) {
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.nodeObject = nodeObject;
  }

  public String getNodeId() {
    return this.nodeId;
  }

  public String getNodeName() {
    return this.nodeName;
  }
}
