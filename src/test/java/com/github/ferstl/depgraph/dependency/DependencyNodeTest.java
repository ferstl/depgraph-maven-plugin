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
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static com.github.ferstl.depgraph.dependency.NodeResolution.OMITTED_FOR_CONFLICT;
import static com.github.ferstl.depgraph.dependency.NodeResolution.OMITTED_FOR_DUPLICATE;
import static org.eclipse.aether.util.graph.transformer.ConflictResolver.NODE_DATA_WINNER;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for {@link DependencyNode}.
 */
public class DependencyNodeTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void defaultResolutionFromMavenArtifact() {
    // arrange
    DependencyNode adapter = new DependencyNode(createMavenArtifact());

    // assert
    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultResolutionFromAetherDependencyNode() {
    // arrange
    org.eclipse.aether.graph.DependencyNode aetherDependencyNode = createAetherDependencyNode();

    // act
    DependencyNode adapter = new DependencyNode(aetherDependencyNode);

    // assert
    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void duplicateFromAetherDependencyNode() {
    // arrange
    org.eclipse.aether.graph.DependencyNode aetherDependencyNode = createAetherDependencyNode();
    DefaultDependencyNode winner = new DefaultDependencyNode(aetherDependencyNode);
    aetherDependencyNode.setData(NODE_DATA_WINNER, winner);

    // act
    DependencyNode adapter = new DependencyNode(aetherDependencyNode);

    // assert
    assertEquals(OMITTED_FOR_DUPLICATE, adapter.getResolution());
  }

  @Test
  public void conflictFromAetherDependencyNode() {
    // arrange
    org.eclipse.aether.graph.DependencyNode aetherDependencyNode = createAetherDependencyNode();
    org.eclipse.aether.artifact.Artifact winnerArtifact = createAetherArtifact();
    winnerArtifact = winnerArtifact.setVersion(winnerArtifact.getVersion() + "-alpha");
    DefaultDependencyNode winner = new DefaultDependencyNode(new Dependency(winnerArtifact, "compile"));

    aetherDependencyNode.setData(NODE_DATA_WINNER, winner);

    // act
    DependencyNode adapter = new DependencyNode(aetherDependencyNode);

    // assert
    assertEquals(OMITTED_FOR_CONFLICT, adapter.getResolution());
  }

  @Test
  public void optionalFromAetherDependencyNode() {
    // arrange
    org.eclipse.aether.graph.DependencyNode aetherDependencyNode = createAetherDependencyNode();
    aetherDependencyNode.setOptional(true);

    // act
    DependencyNode adapter = new DependencyNode(aetherDependencyNode);

    // assert
    assertTrue(adapter.getArtifact().isOptional());
  }

  @Test
  public void mergeNonOptionalIntoOptional() {
    // arrange
    // create an optional dependency
    org.eclipse.aether.graph.DependencyNode optionalNode = createAetherDependencyNode();
    optionalNode.setOptional(true);
    DependencyNode optionalDependencyNode = new DependencyNode(optionalNode);

    org.eclipse.aether.graph.DependencyNode nonOptionalNode = createAetherDependencyNode();
    DependencyNode nonOptionalDependencyNode = new DependencyNode(nonOptionalNode);

    // act
    optionalDependencyNode.merge(nonOptionalDependencyNode);

    // assert
    assertFalse(optionalDependencyNode.getArtifact().isOptional());
  }


  @Test
  public void defaultCompileScope() {
    Artifact artifact = createMavenArtifact();
    artifact.setScope(null);

    DependencyNode adapter = new DependencyNode(artifact);
    assertEquals("compile", adapter.getArtifact().getScope());
  }

  @Test
  public void effectiveScope() {
    // arrange
    Artifact artifact1 = createMavenArtifact();
    artifact1.setScope("runtime");
    Artifact artifact2 = createMavenArtifact();
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


  private Artifact createMavenArtifact() {
    return new DefaultArtifact("groupId", "artifactId", "1.0.0", "compile", "jar", "", null);
  }

  private org.eclipse.aether.artifact.DefaultArtifact createAetherArtifact() {
    return new org.eclipse.aether.artifact.DefaultArtifact("groupId", "artifactId", "jar", "1.0.0");
  }

  private org.eclipse.aether.graph.DependencyNode createAetherDependencyNode() {
    Dependency dependency = new Dependency(createAetherArtifact(), "compile");
    return new DefaultDependencyNode(dependency);
  }

}
