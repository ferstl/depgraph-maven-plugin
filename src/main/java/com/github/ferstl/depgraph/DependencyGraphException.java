package com.github.ferstl.depgraph;

import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

/**
 * Wrapper for {@link DependencyGraphException} and {@link DependencyTreeBuilderException}.
 */
public final class DependencyGraphException extends RuntimeException {

  private static final long serialVersionUID = 4167396359488785529L;

  public DependencyGraphException(DependencyGraphBuilderException cause) {
    super(cause);
  }

  public DependencyGraphException(DependencyTreeBuilderException cause) {
    super(cause);
  }
}
