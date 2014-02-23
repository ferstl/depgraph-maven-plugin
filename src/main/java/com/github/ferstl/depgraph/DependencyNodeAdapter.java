package com.github.ferstl.depgraph;

import org.apache.maven.artifact.Artifact;

import com.github.ferstl.depgraph.dot.Node;

/**
 * {@link Node} implementation that adapts to:
 * <ul>
 * <li>{@link org.apache.maven.artifact.Artifact}</li>
 * <li>{@link org.apache.maven.shared.dependency.graph.DependencyNode}</li>
 * <li>{@link org.apache.maven.shared.dependency.tree.DependencyNode}</li>
 * </ul>
 */
public class DependencyNodeAdapter implements Node {

  private final Artifact artifact;


  public DependencyNodeAdapter(Artifact artifact) {
    if (artifact == null) {
      throw new NullPointerException("Artifact must not be null");
    }

    // FIXME: better create a copy of the artifact and set the scope there.
    if (artifact.getScope() == null) {
      artifact.setScope("compile");
    }

    this.artifact = artifact;
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
  }

  @Override
  public Artifact getArtifact() {
    return this.artifact;
  }
}
