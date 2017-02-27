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
package com.github.ferstl.depgraph.dependency;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import com.github.ferstl.depgraph.dot.DotBuilder;

import static java.util.EnumSet.allOf;


/**
 * A node visitor that creates edges between the visited nodes using a {@link DotBuilder}. This class implements the
 * {@code DependencyNodeVisitor} interfaces for dependency trees and dependency graphs and adapts the different node
 * instances using {@link DependencyNode}.
 */
class GraphBuildingVisitor implements org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor, org.apache.maven.shared.dependency.tree.traversal.DependencyNodeVisitor {

  private final DotBuilder<DependencyNode> dotBuilder;
  private final Deque<DependencyNode> stack;
  private final ArtifactFilter globalFilter;
  private final ArtifactFilter targetFilter;
  private final Set<NodeResolution> includedResolutions;

  GraphBuildingVisitor(DotBuilder<DependencyNode> dotBuilder, ArtifactFilter globalFilter, ArtifactFilter targetFilter, Set<NodeResolution> includedResolutions) {
    this.dotBuilder = dotBuilder;
    this.stack = new ArrayDeque<>();
    this.globalFilter = globalFilter;
    this.targetFilter = targetFilter;
    this.includedResolutions = includedResolutions;
  }

  GraphBuildingVisitor(DotBuilder<DependencyNode> dotBuilder, ArtifactFilter targetFilter) {
    this(dotBuilder, DoNothingArtifactFilter.INSTANCE, targetFilter, allOf(NodeResolution.class));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalVisit(new DependencyNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.graph.DependencyNode node) {
    return internalEndVisit(new DependencyNode(node));
  }

  @Override
  public boolean visit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalVisit(new DependencyNode(node));
  }

  @Override
  public boolean endVisit(org.apache.maven.shared.dependency.tree.DependencyNode node) {
    return internalEndVisit(new DependencyNode(node));
  }

  private boolean internalVisit(DependencyNode node) {
    DependencyNode currentParent = this.stack.peek();

    if (this.globalFilter.include(node.getArtifact()) && leadsToTargetDependency(node)) {
      if (currentParent != null && this.includedResolutions.contains(node.getResolution())) {
        mergeWithExisting(node);

        this.dotBuilder.addEdge(currentParent, node);
      }

      this.stack.push(node);

      return true;
    }

    return false;
  }

  private void mergeWithExisting(DependencyNode node) {
    DependencyNode effectiveNode = this.dotBuilder.getEffectiveNode(node);
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
