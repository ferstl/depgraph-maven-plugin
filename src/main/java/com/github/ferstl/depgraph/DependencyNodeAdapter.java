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
  private final NodeResolution resolution;


  public  DependencyNodeAdapter(Artifact artifact) {
    this(artifact, NodeResolution.INCLUDED);
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.graph.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact());
  }

  public DependencyNodeAdapter(org.apache.maven.shared.dependency.tree.DependencyNode dependencyNode) {
    this(dependencyNode.getArtifact(), determineResolution(dependencyNode.getState()));
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

  private static NodeResolution determineResolution(int res) {
    switch (res) {
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_DUPLICATE:
        return NodeResolution.OMITTED_FOR_DUPLICATE;
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CONFLICT:
        return NodeResolution.OMMITTED_FOR_CONFLICT;
      case org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CYCLE:
        return NodeResolution.OMMITTED_FOR_CYCLE;
      default:
        return NodeResolution.INCLUDED;
    }
  }
}
