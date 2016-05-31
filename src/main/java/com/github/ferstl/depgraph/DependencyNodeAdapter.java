/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
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
package com.github.ferstl.depgraph;

import java.util.Collection;
import java.util.Objects;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dot.Node;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * {@link Node} implementation that adapts to:
 * <ul>
 * <li>{@link org.apache.maven.artifact.Artifact}</li>
 * <li>{@link org.apache.maven.shared.dependency.graph.DependencyNode}</li>
 * <li>{@link org.apache.maven.shared.dependency.tree.DependencyNode}</li>
 * </ul>
 */
public class DependencyNodeAdapter implements Node {

  private org.apache.maven.shared.dependency.graph.DependencyNode graphNode;
  private org.apache.maven.shared.dependency.tree.DependencyNode treeNode;
  private final Artifact artifact;
  private final NodeResolution resolution;


  public DependencyNodeAdapter(Artifact artifact) {
    this(artifact, NodeResolution.INCLUDED);
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
    this.graphNode = dependencyNode;
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact(), determineResolution(dependencyNode.getState()));
    this.treeNode = dependencyNode;
  }

  private DependencyNodeAdapter(Artifact artifact, NodeResolution resolution) {
    if (artifact == null) {
      throw new NullPointerException("Artifact must not be null");
    }

    // FIXME: better create a copy of the artifact and set the scope there.
    if (artifact.getScope() == null) {
      artifact.setScope("compile");
    }

    this.artifact = artifact;
    this.resolution = resolution;
  }

  @Override
  public Artifact getArtifact() {
    return this.artifact;
  }

  @Override
  public NodeResolution getResolution() {
    return this.resolution;
  }

  public Collection<DependencyNodeAdapter> getChildren() {
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
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (!(obj instanceof DependencyNodeAdapter)) { return false; }

    DependencyNodeAdapter other = (DependencyNodeAdapter) obj;

    return Objects.equals(this.artifact, other.artifact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.artifact);
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

  private static enum TreeNode2Adapter
      implements Function<org.apache.maven.shared.dependency.tree.DependencyNode, DependencyNodeAdapter> {
    INSTANCE;

    @Override
    public DependencyNodeAdapter apply(org.apache.maven.shared.dependency.tree.DependencyNode tn) {
      return new DependencyNodeAdapter(tn);
    }
  }

  private static enum GraphNode2Adapter
      implements Function<org.apache.maven.shared.dependency.graph.DependencyNode, DependencyNodeAdapter> {
    INSTANCE;

    @Override
    public DependencyNodeAdapter apply(org.apache.maven.shared.dependency.graph.DependencyNode tn) {
      return new DependencyNodeAdapter(tn);
    }
  }
}
