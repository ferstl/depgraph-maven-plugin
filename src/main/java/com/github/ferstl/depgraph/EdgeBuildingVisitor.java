package com.github.ferstl.depgraph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;


// Invariants
// - Stack is empty after a root node has been visited
public class EdgeBuildingVisitor implements DependencyNodeVisitor {

  private final Set<Edge> edges;
  private final Deque<DependencyNode> stack;
  private final DependencyNodeRenderer renderer;


  public EdgeBuildingVisitor(DependencyNodeRenderer renderer) {
    this.edges = new LinkedHashSet<>();
    this.stack = new ArrayDeque<>();

    this.renderer = renderer;
  }

  public Set<Edge> getEdges() {
    return this.edges;
  }

  @Override
  public boolean visit(DependencyNode node) {
    DependencyNode currentParent = this.stack.peek();

    if (currentParent != null) {
      String from = this.renderer.render(currentParent);
      String to = this.renderer.render(node);
      Edge edge = new Edge(from, to);
      this.edges.add(edge);
    }

    this.stack.push(node);

    return true;
  }

  @Override
  public boolean endVisit(DependencyNode node) {
    this.stack.pop();

    return true;
  }

  @Override
  public String toString() {
    StringBuilder dotBuilder = new StringBuilder();

    for (Edge edge : this.edges) {
      dotBuilder.append("\n  \"").append(edge);
    }

    // Strip the leading newline character
    if (dotBuilder.length() > 0) {
      return dotBuilder.substring(1);
    }

    return "";
  }
}
