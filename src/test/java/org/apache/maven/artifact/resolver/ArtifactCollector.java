package org.apache.maven.artifact.resolver;

import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

/**
 * Dummy interface that must be on the class path when mocking {@link DependencyTreeBuilder}.
 * It is used in a deprecated method in {@link DependencyTreeBuilder}.
 */
public interface ArtifactCollector {

}
