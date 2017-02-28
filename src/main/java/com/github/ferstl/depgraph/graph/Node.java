package com.github.ferstl.depgraph.graph;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Node)) {
      return false;
    }

    Node<?> other = (Node<?>) o;
    return Objects.equals(this.nodeId, other.nodeId)
        && Objects.equals(this.nodeName, other.nodeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.nodeId, this.nodeName);
  }

  @Override
  public String toString() {
    return this.nodeId + "(" + this.nodeName + ")";
  }
}
