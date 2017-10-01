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
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link DependencyNode}.
 */
public class DependencyNodeTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void defaultResolutionFromArtifact() {
    DependencyNode adapter = new DependencyNode(createArtifact());
    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultResolutionFromDependencyGraphNode() {
    org.apache.maven.shared.dependency.graph.DependencyNode node =
        new org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode(null, createArtifact(), "", "", "");
    DependencyNode adapter = new DependencyNode(node);

    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultResolutionFromDependencyTreeNode() {
    org.apache.maven.shared.dependency.tree.DependencyNode node =
        new org.apache.maven.shared.dependency.tree.DependencyNode(createArtifact());
    DependencyNode adapter = new DependencyNode(node);

    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void resolutionFromDependencyTreeNode() {
    Artifact artifact = createArtifact();
    Artifact relatedArtifact = createArtifact();

    org.apache.maven.shared.dependency.tree.DependencyNode node =
        new org.apache.maven.shared.dependency.tree.DependencyNode(artifact, org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CONFLICT, relatedArtifact);
    DependencyNode adapter = new DependencyNode(node);
    assertEquals(NodeResolution.OMITTED_FOR_CONFLICT, adapter.getResolution());

    node = new org.apache.maven.shared.dependency.tree.DependencyNode(artifact, org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_DUPLICATE, relatedArtifact);
    adapter = new DependencyNode(node);
    assertEquals(NodeResolution.OMITTED_FOR_DUPLICATE, adapter.getResolution());

    node = new org.apache.maven.shared.dependency.tree.DependencyNode(artifact, org.apache.maven.shared.dependency.tree.DependencyNode.OMITTED_FOR_CYCLE);
    adapter = new DependencyNode(node);
    assertEquals(NodeResolution.OMITTED_FOR_CYCLE, adapter.getResolution());
  }

  @Test
  public void defaultCompileScope() {
    Artifact artifact = createArtifact();
    artifact.setScope(null);

    DependencyNode adapter = new DependencyNode(artifact);
    assertEquals("compile", adapter.getArtifact().getScope());
  }

  @Test
  public void effectiveScope() {
    // arrange
    Artifact artifact1 = createArtifact();
    artifact1.setScope("runtime");
    Artifact artifact2 = createArtifact();
    artifact2.setScope("provided");

    // act
    DependencyNode node = new DependencyNode(artifact1);
    node.merge(new DependencyNode(artifact2));

    // assert
    assertEquals("provided", node.getEffectiveScope());
  }

  @Test
  public void nullArtifact() {
    this.expectedException.expect(NullPointerException.class);
    this.expectedException.expectMessage(not(emptyString()));

    new DependencyNode((Artifact) null);
  }


  private Artifact createArtifact() {
    return new DefaultArtifact("groupId", "artifactId", "1.0.0", "compile", "jar", "", null);
  }

}
