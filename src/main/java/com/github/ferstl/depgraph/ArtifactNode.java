package com.github.ferstl.depgraph;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;

/**
 * An artificial {@link DependencyNode} that wraps a regular {@link Artifact}. This class is used
 * for parent {@link MavenProject}s that are not part of a dependency graph.
 */
class ArtifactNode implements DependencyNode {

  private static final String UNSUPPORTED_OPERATION_MESSAGE =
      "This operation is not supportet in instances of " + ArtifactNode.class;

  private final Artifact artifact;

  public ArtifactNode(Artifact artifact) {
    if (artifact == null) {
      throw new NullPointerException("Artifact must not be null");
    }

    // FIXME: better create a copy of the artifact and set the scope there.
    artifact.setScope("compile");
    this.artifact = artifact;
  }

  @Override
  public Artifact getArtifact() {
    return this.artifact;
  }

  @Override
  public List<DependencyNode> getChildren() {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
  }

  @Override
  public boolean accept(DependencyNodeVisitor visitor) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
  }

  @Override
  public DependencyNode getParent() {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
  }

  @Override
  public String getPremanagedVersion() {
    return this.artifact.getVersion();
  }

  @Override
  public String getPremanagedScope() {
    return this.artifact.getScope();
  }

  @Override
  public String getVersionConstraint() {
    return this.artifact.getVersion();
  }

  @Override
  public String toNodeString() {
    return this.artifact.toString();
  }

}
