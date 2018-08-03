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
import java.util.HashSet;
import java.util.Set;

import com.github.ferstl.depgraph.graph.Edge;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.eclipse.aether.graph.DependencyVisitor;
import com.github.ferstl.depgraph.graph.GraphBuilder;


/**
 * A node visitor that creates edges between the visited nodes using a {@link GraphBuilder}.
 */
class GraphBuildingVisitor implements DependencyVisitor {

  private final GraphBuilder<DependencyNode> graphBuilder;
  private final boolean omitReachablePaths;
  private final Deque<DependencyNode> nodeStack;
  private final ArtifactFilter globalFilter;
  private final ArtifactFilter transitiveFilter;
  private final ArtifactFilter targetFilter;
  private final Set<NodeResolution> includedResolutions;

  /**
   * Max depth of the graph. Nodes deeper than this depth will be cut off from the graph.
   */
  private int cutOffDepth = 0;

  GraphBuildingVisitor(GraphBuilder<DependencyNode> graphBuilder, ArtifactFilter globalFilter, ArtifactFilter transitiveFilter, ArtifactFilter targetFilter, Set<NodeResolution> includedResolutions, boolean omitReachablePaths) {
    this.graphBuilder = graphBuilder;
    this.omitReachablePaths = omitReachablePaths;
    this.nodeStack = new ArrayDeque<>();
    this.globalFilter = globalFilter;
    this.transitiveFilter = transitiveFilter;
    this.targetFilter = targetFilter;
    this.includedResolutions = includedResolutions;
  }

  @Override
  public boolean visitEnter(org.eclipse.aether.graph.DependencyNode node) {
    DependencyNode node1 = new DependencyNode(node);
    if (isExcluded(node1)) {
      return false;
    }

    this.nodeStack.push(node1);

    if (this.targetFilter.include(node1.getArtifact())) {
      this.cutOffDepth = this.nodeStack.size();
    }

    return true;
  }

  @Override
  public boolean visitLeave(org.eclipse.aether.graph.DependencyNode node) {
    DependencyNode dependencyNode = new DependencyNode(node);
    if (isExcluded(dependencyNode)) {
      return true;
    }

    this.nodeStack.pop();

    DependencyNode currentParent = this.nodeStack.peek();
    if (this.nodeStack.size() < this.cutOffDepth) {
      this.cutOffDepth = this.nodeStack.size();

      if (currentParent != null) {
        mergeWithExisting(dependencyNode);
        this.graphBuilder.addEdge(currentParent, dependencyNode);
      }
    }

    if (this.nodeStack.size() == 0 && this.omitReachablePaths) {
      this.removeRedundantEdges();
    }

    return true;
  }

  private void removeRedundantEdges() {
    Set<Edge> toRemove = new HashSet<>();

    for (Edge edge : this.graphBuilder.getEdges()) {
      if ("test".equals(this.graphBuilder.getNode(edge.getToNodeId()).getArtifact().getScope())) {
        continue;
      }

      if (this.graphBuilder.hasAlternativePath(edge)) {
        toRemove.add(edge);
      }
    }

    for (Edge edge : toRemove) {
      this.graphBuilder.removeEdge(edge);
    }
  }


  private boolean isExcluded(DependencyNode node) {
    Artifact artifact = node.getArtifact();

    return !this.globalFilter.include(artifact)
        || !this.transitiveFilter.include(artifact)
        || !this.includedResolutions.contains(node.getResolution());
  }

  private void mergeWithExisting(DependencyNode node) {
    DependencyNode effectiveNode = this.graphBuilder.getEffectiveNode(node);
    node.merge(effectiveNode);
  }
}
