package com.github.ferstl.depgraph.graph;

import java.util.Collection;

public class TestFormatter implements GraphFormatter {

  public String graphName;
  public Collection<Node<?>> nodes;
  public Collection<Edge> edges;

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    this.graphName = graphName;
    this.nodes = nodes;
    this.edges = edges;
    return "";
  }
}
