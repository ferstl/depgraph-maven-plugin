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

import org.apache.maven.artifact.Artifact;

import static org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CONFLICT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DependencyNodeUtil {

  private DependencyNodeUtil() {
    throw new AssertionError("not instantiable");
  }

  public static DependencyNode createDependencyNodeWithConflict(String groupId, String artifactId, String effectiveVersion) {
    return createDependencyNodeWithConflict(groupId, artifactId, effectiveVersion, "compile");
  }

  public static DependencyNode createDependencyNodeWithConflict(String groupId, String artifactId, String effectiveVersion, String scope) {
    Artifact artifact = createArtifact(groupId, artifactId, effectiveVersion, scope, "jar", "");
    Artifact conflictingArtifact = mock(Artifact.class);
    when(conflictingArtifact.getVersion()).thenReturn(effectiveVersion + "-alpha");

    org.apache.maven.shared.dependency.tree.DependencyNode mavenDependencyNode = new org.apache.maven.shared.dependency.tree.DependencyNode(artifact, OMITTED_FOR_CONFLICT, conflictingArtifact);
    return new DependencyNode(mavenDependencyNode);
  }

  public static DependencyNode createDependencyNode(String groupId, String artifactId, String version) {
    return new DependencyNode(createArtifact(groupId, artifactId, version));
  }

  public static DependencyNode createDependencyNode(String groupId, String artifactId, String version, String scope) {
    return new DependencyNode(createArtifact(groupId, artifactId, version, scope, "jar", ""));
  }

  public static void addTypes(DependencyNode dependencyNode, String... types) {
    for (String type : types) {
      Artifact artifact = dependencyNode.getArtifact();
      DependencyNode nodeWithClassifier = new DependencyNode(createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope(), type, artifact.getClassifier()));
      dependencyNode.merge(nodeWithClassifier);
    }
  }

  public static void addClassifiers(DependencyNode dependencyNode, String... classifiers) {
    for (String classifier : classifiers) {
      Artifact artifact = dependencyNode.getArtifact();
      DependencyNode nodeWithClassifier = new DependencyNode(createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope(), "jar", classifier));
      dependencyNode.merge(nodeWithClassifier);
    }
  }

  private static Artifact createArtifact(String groupId, String artifactId, String version) {
    return createArtifact(groupId, artifactId, version, "compile", "jar", "");
  }

  private static Artifact createArtifact(String groupId, String artifactId, String version, String scope, String type, String classifier) {
    Artifact artifact = mock(Artifact.class);
    when(artifact.getGroupId()).thenReturn(groupId);
    when(artifact.getArtifactId()).thenReturn(artifactId);
    when(artifact.getVersion()).thenReturn(version);
    when(artifact.getScope()).thenReturn(scope);
    when(artifact.getType()).thenReturn(type);
    when(artifact.getClassifier()).thenReturn(classifier);
    return artifact;
  }
}
