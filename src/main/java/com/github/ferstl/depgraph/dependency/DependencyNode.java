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
import org.apache.maven.artifact.DefaultArtifact;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import com.google.common.collect.ImmutableSet;

import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;
import static com.github.ferstl.depgraph.dependency.NodeResolution.OMITTED_FOR_CONFLICT;
import static com.github.ferstl.depgraph.dependency.NodeResolution.OMITTED_FOR_DUPLICATE;
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

  private org.apache.maven.shared.dependency.tree.DependencyNode treeNode;
  private final Artifact artifact;
  private String effectiveVersion;
  private final NodeResolution resolution;
  private final Set<String> scopes;
  private final Set<String> classifiers;
  private final Set<String> types;


  public DependencyNode(Artifact artifact) {
    this(artifact, INCLUDED);
    this.effectiveVersion = artifact.getVersion();
  }

  public DependencyNode(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
  }

  public DependencyNode(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact(), determineResolution(dependencyNode.getState()));
    this.effectiveVersion = dependencyNode.getArtifact().getVersion();
    this.treeNode = dependencyNode;
  }

  public DependencyNode(org.eclipse.aether.graph.DependencyNode dependencyNode) {
    this(createMavenArtifact(dependencyNode), determineResolution(dependencyNode));
    this.effectiveVersion = determineEffectiveVersion(dependencyNode);
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

    if (!isNullOrEmpty(artifact.getClassifier())) {
      this.classifiers.add(artifact.getClassifier());
    }
    this.effectiveVersion = artifact.getVersion();
  }

  public void merge(DependencyNode other) {
    if (this == other) {
      return;
    }

    this.scopes.addAll(other.scopes);
    this.classifiers.addAll(other.classifiers);
    this.types.addAll(other.types);
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

  public Set<String> getClassifiers() {
    return ImmutableSet.copyOf(this.classifiers);
  }

  public Set<String> getTypes() {
    return ImmutableSet.copyOf(this.types);
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
      return this.effectiveVersion;
    }

    return this.treeNode.getRelatedArtifact().getVersion();
  }

  /**
   * Returns the <strong>effective</strong> scope of this node, i.e. the scope that is actually used. This is important
   * if scopes are merged and a node may have more than one scope.
   *
   * @return The effective scope of this node.
   */
  public String getEffectiveScope() {
    if (this.scopes.size() > 0) {
      return this.scopes.iterator().next();
    }

    // should never happen
    return null;
  }


  @Override
  public String toString() {
    return this.artifact.toString();
  }

  private static Artifact createMavenArtifact(org.eclipse.aether.graph.DependencyNode dependencyNode) {
    org.eclipse.aether.artifact.Artifact artifact = dependencyNode.getArtifact();
    String scope = null;
    if (dependencyNode.getDependency() != null) {
      scope = dependencyNode.getDependency().getScope();
    }

    return new DefaultArtifact(
        artifact.getGroupId(),
        artifact.getArtifactId(),
        artifact.getVersion(),
        scope,
        artifact.getProperty("type", artifact.getExtension()),
        artifact.getClassifier(),
        null
    );
  }

  private static NodeResolution determineResolution(org.eclipse.aether.graph.DependencyNode dependencyNode) {
    org.eclipse.aether.graph.DependencyNode winner = (org.eclipse.aether.graph.DependencyNode) dependencyNode.getData().get(ConflictResolver.NODE_DATA_WINNER);

    if (winner != null) {
      if (winner.getArtifact().getVersion().equals(dependencyNode.getArtifact().getVersion())) {
        return OMITTED_FOR_DUPLICATE;
      }

      return OMITTED_FOR_CONFLICT;
    }

    return INCLUDED;
  }

  private static String determineEffectiveVersion(org.eclipse.aether.graph.DependencyNode dependencyNode) {
    org.eclipse.aether.graph.DependencyNode winner = (org.eclipse.aether.graph.DependencyNode) dependencyNode.getData().get(ConflictResolver.NODE_DATA_WINNER);
    if (winner != null) {
      return winner.getArtifact().getVersion();
    }

    return dependencyNode.getArtifact().getVersion();
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
        return INCLUDED;
    }
  }
}
