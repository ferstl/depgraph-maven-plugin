package com.github.ferstl.depgraph.graph;

import java.util.Objects;

public final class Edge {

  private final String fromNodeId;
  private final String toNodeId;
  private final String name;

  Edge(String fromNodeId, String toNodeId, String name) {
    this.fromNodeId = fromNodeId;
    this.toNodeId = toNodeId;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof Edge)) { return false; }

    Edge edge = (Edge) o;
    return Objects.equals(this.fromNodeId, edge.fromNodeId)
        && Objects.equals(this.toNodeId, edge.toNodeId)
        && Objects.equals(this.name, edge.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fromNodeId, this.toNodeId, this.name);
  }

  public String getFromNodeId() {
    return this.fromNodeId;
  }

  public String getToNodeId() {
    return this.toNodeId;
  }

  public String getName() {
    return this.name;
  }
}
