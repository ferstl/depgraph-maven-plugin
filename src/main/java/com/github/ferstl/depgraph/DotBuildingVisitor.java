package com.github.ferstl.depgraph;

import java.util.ArrayDeque;
import java.util.Deque;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.dot.Node;


// Invariants
// - Stack is empty after a root node has been visited
class DotBuildingVisitor implements org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor, org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor {

  private final DotBuilder dotBuilder;
  private final Deque<Node> stack;


  public DotBuildingVisitor(DotBuilder dotBuilder) {
    this.dotBuilder = dotBuilder;
    this.stack = new ArrayDeque<>();
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalVisit(new DependencyNodeAdapter(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalEndVisit(new DependencyNodeAdapter(node));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalVisit(new DependencyNodeAdapter(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalEndVisit(new DependencyNodeAdapter(node));
  }

  private boolean internalVisit(Node node) {
    Node currentParent = this.stack.peek();

    if (currentParent != null) {
      this.dotBuilder.addEdge(currentParent, node);
    }

    this.stack.push(node);

    return true;
  }

  private boolean internalEndVisit(Node node) {
    this.stack.pop();

    return true;
  }
}
