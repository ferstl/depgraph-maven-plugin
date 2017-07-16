/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
package com.github.ferstl.depgraph.dependency;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import static java.lang.Math.max;
import static java.util.EnumSet.allOf;


/**
 * A node visitor that creates edges between the visited nodes using a {@link GraphBuilder}. This class implements the
 * {@code DependencyNodeVisitor} interfaces for dependency trees and dependency graphs and adapts the different node
 * instances using {@link DependencyNode}.
 */
class GraphBuildingVisitor implements org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor, org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor {

  private final GraphBuilder<DependencyNode> graphBuilder;
  private final Deque<DependencyNode> nodeStack;
  private final ArtifactFilter globalFilter;
  private final ArtifactFilter targetFilter;
  private final Set<NodeResolution> includedResolutions;

  /**
   * Max depth of the graph. Nodes deeper than this depth will be cut off from the graph.
   */
  private int cutOffDepth = 0;

  GraphBuildingVisitor(GraphBuilder<DependencyNode> graphBuilder, ArtifactFilter globalFilter, ArtifactFilter targetFilter, Set<NodeResolution> includedResolutions) {
    this.graphBuilder = graphBuilder;
    this.nodeStack = new ArrayDeque<>();
    this.globalFilter = globalFilter;
    this.targetFilter = targetFilter;
    this.includedResolutions = includedResolutions;
  }

  GraphBuildingVisitor(GraphBuilder<DependencyNode> graphBuilder, ArtifactFilter targetFilter) {
    this(graphBuilder, DoNothingArtifactFilter.INSTANCE, targetFilter, allOf(NodeResolution.class));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalVisit2(new DependencyNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalEndVisit2(new DependencyNode(node));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalVisit2(new DependencyNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalEndVisit2(new DependencyNode(node));
  }

  private boolean internalVisit(DependencyNode node) {
    DependencyNode currentParent = this.nodeStack.peek();

    if (this.globalFilter.include(node.getArtifact()) && leadsToTargetDependency(node)) {
      if (currentParent != null && this.includedResolutions.contains(node.getResolution())) {
        mergeWithExisting(node);

        this.graphBuilder.addEdge(currentParent, node);
      }

      this.nodeStack.push(node);

      return true;
    }

    return false;
  }

  private boolean internalVisit2(DependencyNode node) {
    if (isIncluded(node)) {
      this.nodeStack.push(node);

      if (this.targetFilter.include(node.getArtifact())) {
        this.cutOffDepth = max(this.cutOffDepth, this.nodeStack.size());
      }

      return true;
    }

    return false;
  }

  private boolean internalEndVisit2(DependencyNode node) {
    if (isIncluded(node)) {
      this.nodeStack.pop();
      DependencyNode currentParent = this.nodeStack.peek();


      if (currentParent == null) {
        // We are at the root element of the graph.
        this.cutOffDepth = 0;
      } else if (this.nodeStack.size() < this.cutOffDepth) {
        this.cutOffDepth = this.nodeStack.size();
        mergeWithExisting(node);
        this.graphBuilder.addEdge(currentParent, node);
      }

      return true;
    }

    return false;
  }

  private boolean isIncluded(DependencyNode node) {
    return this.globalFilter.include(node.getArtifact()) && this.includedResolutions.contains(node.getResolution());
  }

  private void mergeWithExisting(DependencyNode node) {
    DependencyNode effectiveNode = this.graphBuilder.getEffectiveNode(node);
    node.merge(effectiveNode);
  }

  private boolean leadsToTargetDependency(DependencyNode node) {
    if (this.targetFilter.include(node.getArtifact())) {
      return true;
    }

    for (DependencyNode c : node.getChildren()) {
      if (leadsToTargetDependency(c)) {
        return true;
      }
    }

    return false;
  }

  private boolean internalEndVisit(DependencyNode node) {
    if (this.globalFilter.include(node.getArtifact()) && leadsToTargetDependency(node)) {
      this.nodeStack.pop();
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
