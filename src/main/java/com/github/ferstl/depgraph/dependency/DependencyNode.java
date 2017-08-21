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

import java.util.Set;
import java.util.TreeSet;
import org.apache.maven.artifact.Artifact;
import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Representation of a dependency graph node. It adapts these Maven-specific classes:
 * <ul>
 * <li>{@link org.apache.maven.artifact.Artifact}</li>
 * <li>{@link org.apache.maven.shared.dependency.graph.DependencyNode}</li>
 * <li>{@link org.apache.maven.shared.dependency.tree.DependencyNode}</li>
 * </ul>
 */
public final class DependencyNode {

  private org.apache.maven.shared.dependency.graph.DependencyNode graphNode;
  private org.apache.maven.shared.dependency.tree.DependencyNode treeNode;
  private final Artifact artifact;
  private final NodeResolution resolution;
  private final Set<String> scopes;
  private final Set<String> classifiers;
  private final Set<String> types;


  public DependencyNode(Artifact artifact) {
    this(artifact, NodeResolution.INCLUDED);
  }

  public DependencyNode(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
    this.graphNode = dependencyNode;
  }

  public DependencyNode(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact(), determineResolution(dependencyNode.getState()));
    this.treeNode = dependencyNode;
  }

  private DependencyNode(Artifact artifact, NodeResolution resolution) {
    if (artifact == null) {
      throw new NullPointerException("Artifact must not be null");
    }

    // FIXME: better create a copy of the artifact and set the missing attributes there.
    if (artifact.getScope() == null) {
      artifact.setScope("compile");
    }

    this.scopes = new TreeSet<>();
    this.classifiers = new TreeSet<>();
    this.types = new TreeSet<>();
    this.artifact = artifact;
    this.resolution = resolution;
    this.scopes.add(artifact.getScope());
    this.types.add(artifact.getType());

    if (isNullOrEmpty(artifact.getClassifier())) {
      this.classifiers.add(artifact.getClassifier());
    }
  }

  public void merge(DependencyNode other) {
    if (this == other) {
      return;
    }

    this.scopes.addAll(other.getScopes());
  }

  public Artifact getArtifact() {
    return this.artifact;
  }

  public NodeResolution getResolution() {
    return this.resolution;
  }

  public Set<String> getScopes() {
    return ImmutableSet.copyOf(this.scopes);
  }

  /**
   * Returns the <strong>effective</strong> version of this node, i.e. the version that is actually used. This is
   * important for nodes with a resolution of {@link NodeResolution#OMITTED_FOR_CONFLICT} where
   * {@code getArtifact().getVersion()} will return the omitted version.
   *
   * @return The effective version of this node.
   */
  public String getEffectiveVersion() {
    if (this.treeNode == null || this.treeNode.getRelatedArtifact() == null) {
      return this.artifact.getVersion();
    }

    return this.treeNode.getRelatedArtifact().getVersion();
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
}
