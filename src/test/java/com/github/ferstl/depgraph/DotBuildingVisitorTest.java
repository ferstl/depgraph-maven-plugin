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

import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodesAndEdges;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.ferstl.depgraph.dot.DotBuilder;


public class DotBuildingVisitorTest {

  private DotBuilder dotBuilder;
  private DotBuildingVisitor visitor;
  private ArtifactFilter artifactFilter;
  private ArtifactFilter targetDependencies;

  @Before
  public void before() {
    this.dotBuilder = new DotBuilder();

    this.artifactFilter = mock(ArtifactFilter.class);
    when(this.artifactFilter.include(Mockito.<Artifact>any())).thenReturn(true);

    // this is the same as an empty list of target dependencies
    this.targetDependencies = mock(ArtifactFilter.class);
    when(this.targetDependencies.include(Mockito.<Artifact>any())).thenReturn(true);

    this.visitor = new DotBuildingVisitor(this.dotBuilder, this.artifactFilter, this.targetDependencies);
  }

  /**
   * <pre>
   * parent
   * - child
   * </pre>
   */
  @Test
  public void parentAndChild() {
    DependencyNode child = createGraphNode("child");
    DependencyNode parent = createGraphNode("parent", child);

    assertTrue(this.visitor.visit(parent));
    assertTrue(this.visitor.visit(child));
    assertTrue(this.visitor.endVisit(child));
    assertTrue(this.visitor.endVisit(parent));

    assertThat(this.dotBuilder, hasNodesAndEdges(
      new String[] {
        "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
        "\"groupId:child:jar:version:compile\"[label=\"groupId:child:jar:version:compile\"]"},
      new String[] {
        "\"groupId:parent:jar:version:compile\" -> \"groupId:child:jar:version:compile\""}));
  }

  /**
   * <pre>
   * parent
   * - child1
   * - child2 (ignored)
   * </pre>
   */
  @Test
  public void ignoredNode() {
    DependencyNode child1 = createGraphNode("child1");
    DependencyNode child2 = createGraphNode("child2");
    DependencyNode parent = createGraphNode("parent", child1, child2);

    when(this.artifactFilter.include(child2.getArtifact())).thenReturn(false);

    assertTrue(this.visitor.visit(parent));
    assertTrue(this.visitor.visit(child1));
    assertTrue(this.visitor.endVisit(child1));

    // Don't process any further children of child2
    assertFalse(this.visitor.visit(child2));

    assertTrue(this.visitor.endVisit(child2));

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] {
          "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
          "\"groupId:child:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]"},
        new String[] {
          "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\""}));
  }

  /**
   * <pre>
   * parent
   * - child1
   * - child2 (target dependency)
   * </pre>
   */
  @Test
  public void targetDepNode() {
    DependencyNode child1 = createGraphNode("child1");
    DependencyNode child2 = createGraphNode("child2");
    DependencyNode parent = createGraphNode("parent", child1, child2);
    
    when(this.targetDependencies.include(Mockito.<Artifact>any())).thenReturn(false);
    when(this.targetDependencies.include(child2.getArtifact())).thenReturn(true);

    assertTrue(this.visitor.visit(parent));

    // Don't process any further children of child2
    assertFalse(this.visitor.visit(child1));
    assertTrue(this.visitor.endVisit(child1));

    assertTrue(this.visitor.visit(child2));
    assertTrue(this.visitor.endVisit(child2));

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] {
          "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
          "\"groupId:child:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
        new String[] {
          "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\""}));
  }
  
  @Test
  public void defaultArtifactFilter() {
    this.visitor = new DotBuildingVisitor(this.dotBuilder, this.targetDependencies);

    // Use other test (I know this is ugly...)
    parentAndChild();
  }

  private static DependencyNode createGraphNode(String artifactId, DependencyNode... children) {
    DependencyNode node = mock(DependencyNode.class);
    when(node.getArtifact()).thenReturn(createArtifact(artifactId));
    when(node.getChildren()).thenReturn(Arrays.asList(children));
    return node;
  }

  private static Artifact createArtifact(String artifactId) {
    return new DefaultArtifact("groupId", artifactId, "version", "compile", "jar", "", null);
  }

}
