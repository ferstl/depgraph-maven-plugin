package com.github.ferstl.depgraph;

import java.util.Objects;

import org.apache.maven.shared.dependency.graph.DependencyNode;

class Edge {
  final DependencyNode from;
  final DependencyNode to;

  public Edge(DependencyNode from, DependencyNode to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.from.getArtifact(), this.to.getArtifact());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!(obj instanceof Edge)) {return false; }

    Edge other = (Edge) obj;

    return Objects.equals(this.from.getArtifact(), other.from.getArtifact())
        && Objects.equals(this.to.getArtifact(), other.to.getArtifact());
  }

  @Override
  public String toString() {
    return "\"" + this.from + "\" -> \"" + this.to + "\";";
  }
}