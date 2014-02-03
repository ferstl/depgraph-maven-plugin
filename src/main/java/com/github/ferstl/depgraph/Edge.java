package com.github.ferstl.depgraph;

import java.util.Objects;

class Edge {
  private final String from;
  private final String to;

  public Edge(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.from, this.to);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) { return true; }
    if (!(obj instanceof Edge)) {return false; }

    Edge other = (Edge) obj;

    return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
  }

  @Override
  public String toString() {
    return "\"" + this.from + "\" -> \"" + this.to + "\";";
  }
}