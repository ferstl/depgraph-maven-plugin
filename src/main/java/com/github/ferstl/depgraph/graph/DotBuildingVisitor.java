/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import com.github.ferstl.depgraph.dot.DotBuilder;


/**
 * A node visitor that creates edges between the visited nodes using a {@link DotBuilder}. This class implements the
 * {@code DependencyNodeVisitor} interfaces for dependency trees and dependency graphs and adapts the different node
 * instances using {@link GraphNode}.
 */
class DotBuildingVisitor implements org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor, org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor {

  private final DotBuilder<GraphNode> dotBuilder;
  private final Deque<GraphNode> stack;
  private final ArtifactFilter globalFilter;
  private final ArtifactFilter targetFilter;

  DotBuildingVisitor(DotBuilder<GraphNode> dotBuilder, ArtifactFilter globalFilter, ArtifactFilter targetFilter) {
    this.dotBuilder = dotBuilder;
    this.stack = new ArrayDeque<>();
    this.globalFilter = globalFilter;
    this.targetFilter = targetFilter;
  }

  DotBuildingVisitor(DotBuilder<GraphNode> dotBuilder, ArtifactFilter targetFilter) {
    this(dotBuilder, DoNothingArtifactFilter.INSTANCE, targetFilter);
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalVisit(new GraphNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalEndVisit(new GraphNode(node));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalVisit(new GraphNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalEndVisit(new GraphNode(node));
  }

  private boolean internalVisit(GraphNode node) {
    GraphNode currentParent = this.stack.peek();

    if (this.globalFilter.include(node.getArtifact()) && leadsToTargetDependency(node)) {
      if (currentParent != null) {
        GraphNode effectiveSource = this.dotBuilder.getEffectiveNode(currentParent);
        GraphNode effectiveTarget = this.dotBuilder.getEffectiveNode(node);
        effectiveTarget.addScope(node.getArtifact().getScope());

        this.dotBuilder.addEdge(effectiveSource, effectiveTarget);
      }

      this.stack.push(node);

      return true;
    }

    return false;
  }

  private boolean leadsToTargetDependency(GraphNode node) {
    if (this.targetFilter.include(node.getArtifact())) {
      return true;
    }

    for (GraphNode c : node.getChildren()) {
      if (leadsToTargetDependency(c)) {
        return true;
      }
    }

    return false;
  }

  private boolean internalEndVisit(GraphNode node) {
    if (this.globalFilter.include(node.getArtifact()) && leadsToTargetDependency(node)) {
      this.stack.pop();
    }

    return true;
  }

  private enum DoNothingArtifactFilter implements ArtifactFilter {
    INSTANCE;

    @Override
    public boolean include(Artifact artifact) {
      return true;
    }

  }
}
