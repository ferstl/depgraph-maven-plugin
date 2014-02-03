package com.github.ferstl.depgraph;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


public class GraphBuilder {

  private final Set<Edge> edges;


  public GraphBuilder() {
    this.edges = new LinkedHashSet<>();
  }

  public void addEdges(Collection<Edge> edges) {
    this.edges.addAll(edges);
  }

  public void addEdges(Edge... edges) {
    this.edges.addAll(Arrays.asList(edges));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {").append("\n  node [shape=box]");

    for (Edge edge : this.edges) {
      sb.append("\n  ").append(edge);
    }

    return sb.append("\n}").toString();
  }
}
