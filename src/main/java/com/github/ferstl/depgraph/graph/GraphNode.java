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

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.apache.maven.artifact.Artifact;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

/**
 * Representation of a dependency graph node. It adapts these Maven-specific classes:
 * <ul>
 * <li>{@link org.apache.maven.artifact.Artifact}</li>
 * <li>{@link org.apache.maven.shared.dependency.graph.DependencyNode}</li>
 * <li>{@link org.apache.maven.shared.dependency.tree.DependencyNode}</li>
 * </ul>
 */
public final class GraphNode {

  private org.apache.maven.shared.dependency.graph.DependencyNode graphNode;
  private org.apache.maven.shared.dependency.tree.DependencyNode treeNode;
  private final Artifact artifact;
  private final NodeResolution resolution;
  private final TreeSet<String> scopes;


  public GraphNode(Artifact artifact) {
    this(artifact, NodeResolution.INCLUDED);
  }

  public GraphNode(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
    this.graphNode = dependencyNode;
  }

  public GraphNode(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact(), determineResolution(dependencyNode.getState()));
    this.treeNode = dependencyNode;
  }

  private GraphNode(Artifact artifact, NodeResolution resolution) {
    if (artifact == null) {
      throw new NullPointerException("Artifact must not be null");
    }

    // FIXME: better create a copy of the artifact and set the missing attributes there.
    if (artifact.getScope() == null) {
      artifact.setScope("compile");
    }

    this.scopes = new TreeSet<>();
    this.artifact = artifact;
    this.resolution = resolution;
    addScope(artifact.getScope());
  }

  public Artifact getArtifact() {
    return this.artifact;
  }

  public NodeResolution getResolution() {
    return this.resolution;
  }

  public void addScope(String scope) {
    this.scopes.add(scope);
  }

  public Set<String> getScopes() {
    return ImmutableSet.copyOf(this.scopes);
  }

  public Collection<GraphNode> getChildren() {
    if (this.treeNode != null) {
      return Collections2.transform(this.treeNode.getChildren(), TreeNode2Adapter.INSTANCE);
    } else if (this.graphNode != null) {
      return Collections2.transform(this.graphNode.getChildren(), GraphNode2Adapter.INSTANCE);
    } else {
      // impossible case
      return null;
    }
  }

  @Override
  public String toString() {
    return this.artifact.toString();
  }

  private static NodeResolution determineResolution(int res) {
    switch (res) {
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_DUPLICATE:
        return NodeResolution.OMITTED_FOR_DUPLICATE;
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CONFLICT:
        return NodeResolution.OMITTED_FOR_CONFLICT;
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CYCLE:
        return NodeResolution.OMITTED_FOR_CYCLE;
      default:
        return NodeResolution.INCLUDED;
    }
  }

  private enum TreeNode2Adapter implements Function<org.apache.maven.shared.dependency.tree.DependencyNode, GraphNode> {
    INSTANCE;

    @Override
    public GraphNode apply(org.apache.maven.shared.dependency.tree.DependencyNode tn) {
      return new GraphNode(tn);
    }
  }

  private enum GraphNode2Adapter implements Function<org.apache.maven.shared.dependency.graph.DependencyNode, GraphNode> {
    INSTANCE;

    @Override
    public GraphNode apply(org.apache.maven.shared.dependency.graph.DependencyNode tn) {
      return new GraphNode(tn);
    }
  }
}
