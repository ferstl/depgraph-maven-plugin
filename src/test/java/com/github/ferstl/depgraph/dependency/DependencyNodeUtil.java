/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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
import org.apache.maven.artifact.DefaultArtifact;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;

import static org.eclipse.aether.util.graph.transformer.ConflictResolver.NODE_DATA_WINNER;

public final class DependencyNodeUtil {

  private DependencyNodeUtil() {
    throw new AssertionError("not instantiable");
  }

  public static DependencyNode createDependencyNodeWithConflict(String groupId, String artifactId, String effectiveVersion) {
    return createDependencyNodeWithConflict(groupId, artifactId, effectiveVersion, "compile");
  }

  public static DependencyNode createDependencyNodeWithConflict(String groupId, String artifactId, String effectiveVersion, String scope) {
    Artifact artifact = createArtifact(groupId, artifactId, effectiveVersion + "-alpha", scope, "jar", "", false);
    return createConflict(artifact, effectiveVersion);
  }

  public static DependencyNode createDependencyNodeWithDuplicate(String groupId, String artifactId, String version) {
    Artifact artifact = createArtifact(groupId, artifactId, version, "compile", "jar", "", false);
    return createConflict(artifact, artifact.getVersion());
  }

  public static DependencyNode createDependencyNode(String groupId, String artifactId, String version) {
    return new DependencyNode(createArtifact(groupId, artifactId, version, false));
  }

  public static DependencyNode createDependencyNode(String groupId, String artifactId, String version, boolean optional) {
    return new DependencyNode(createArtifact(groupId, artifactId, version, optional));
  }

  public static DependencyNode createDependencyNode(String groupId, String artifactId, String version, String scope) {
    return new DependencyNode(createArtifact(groupId, artifactId, version, scope, "jar", "", false));
  }

  public static void addTypes(DependencyNode dependencyNode, String... types) {
    for (String type : types) {
      Artifact artifact = dependencyNode.getArtifact();
      DependencyNode nodeWithClassifier = new DependencyNode(createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope(), type, artifact.getClassifier(), artifact.isOptional()));
      dependencyNode.merge(nodeWithClassifier);
    }
  }

  public static void addClassifiers(DependencyNode dependencyNode, String... classifiers) {
    for (String classifier : classifiers) {
      Artifact artifact = dependencyNode.getArtifact();
      DependencyNode nodeWithClassifier = new DependencyNode(createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope(), "jar", classifier, artifact.isOptional()));
      dependencyNode.merge(nodeWithClassifier);
    }
  }

  private static Artifact createArtifact(String groupId, String artifactId, String version, boolean optional) {
    return createArtifact(groupId, artifactId, version, "compile", "jar", "", optional);
  }

  private static Artifact createArtifact(String groupId, String artifactId, String version, String scope, String type, String classifier, boolean optional) {
    DefaultArtifact artifact = new DefaultArtifact(groupId, artifactId, version, scope, type, classifier, null);
    artifact.setOptional(optional);
    return artifact;
  }


  private static DependencyNode createConflict(Artifact artifact, String winningVersion) {
    org.eclipse.aether.artifact.DefaultArtifact aetherArtifact = new org.eclipse.aether.artifact.DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), artifact.getType(), artifact.getVersion());
    org.eclipse.aether.artifact.DefaultArtifact winnerArtifact = new org.eclipse.aether.artifact.DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), artifact.getType(), winningVersion);

    DefaultDependencyNode dependencyNode = new DefaultDependencyNode(new Dependency(aetherArtifact, artifact.getScope()));
    dependencyNode.setData(NODE_DATA_WINNER, new DefaultDependencyNode(new Dependency(winnerArtifact, "compile")));

    return new DependencyNode(dependencyNode);
  }
}
