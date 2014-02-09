package com.github.ferstl.depgraph;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;


// Invariants
// - Stack is empty after a root node has been visited
public class DotBuildingVisitor implements DependencyNodeVisitor {

  private final DotBuilder dotBuilder;
  private final Deque<DependencyNode> stack;


  public DotBuildingVisitor(DotBuilder dotBuilder) {
    this.dotBuilder = dotBuilder;
    this.stack = new ArrayDeque<>();
  }

  @Override
  public boolean visit(DependencyNode node) {
    DependencyNode currentParent = this.stack.peek();

    if (currentParent != null) {
      this.dotBuilder.addEdge(currentParent, node);
    }

    this.stack.push(node);

    return true;
  }

  @Override
  public boolean endVisit(DependencyNode node) {
    this.stack.pop();

    return true;
  }
}
