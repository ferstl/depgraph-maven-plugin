/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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

import java.util.Arrays;
import java.util.EnumSet;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import static com.github.ferstl.depgraph.graph.GraphBuilderMatcher.hasNodesAndEdges;
import static java.util.EnumSet.allOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GraphBuildingVisitorTest {

  private GraphBuilder<DependencyNode> graphBuilder;
  private GraphBuildingVisitor visitor;
  private ArtifactFilter globalFilter;
  private ArtifactFilter targetFilter;
  private EnumSet<NodeResolution> includedResolutions;

  @Before
  public void before() {
    this.graphBuilder = new GraphBuilder<>();

    this.globalFilter = mock(ArtifactFilter.class);
    when(this.globalFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    // this is the same as an empty list of target dependencies
    this.targetFilter = mock(ArtifactFilter.class);
    when(this.targetFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    this.includedResolutions = allOf(NodeResolution.class);
    this.visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.targetFilter, this.includedResolutions);
  }

  /**
   * .
   * <pre>
   * parent
   *     - child
   * </pre>
   */
  @Test
  public void parentAndChild() {
    org.apache.maven.shared.dependency.graph.DependencyNode child = createGraphNode("child");
    org.apache.maven.shared.dependency.graph.DependencyNode parent = createGraphNode("parent", child);

    assertTrue(this.visitor.visit(parent));
    assertTrue(this.visitor.visit(child));
    assertTrue(this.visitor.endVisit(child));
    assertTrue(this.visitor.endVisit(parent));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child:jar:version:compile\""}));
  }

  /**
   * .
   * <pre>
   * parent
   *     - child1
   *     - child2(ignored)
   * </pre>
   */
  @Test
  public void ignoredNode() {
    org.apache.maven.shared.dependency.graph.DependencyNode child1 = createGraphNode("child1");
    org.apache.maven.shared.dependency.graph.DependencyNode child2 = createGraphNode("child2");
    org.apache.maven.shared.dependency.graph.DependencyNode parent = createGraphNode("parent", child1, child2);

    when(this.globalFilter.include(child2.getArtifact())).thenReturn(false);

    assertTrue(this.visitor.visit(parent));
    assertTrue(this.visitor.visit(child1));
    assertTrue(this.visitor.endVisit(child1));

    // Don't process any further children of child2
    assertFalse(this.visitor.visit(child2));

    assertTrue(this.visitor.endVisit(child2));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\""}));
  }

  /**
   * .
   * <pre>
   * parent
   * - child1
   * - child2 (target dependency)
   * </pre>
   */
  @Test
  public void targetDepNode() {
    org.apache.maven.shared.dependency.graph.DependencyNode child1 = createGraphNode("child1");
    org.apache.maven.shared.dependency.graph.DependencyNode child2 = createGraphNode("child2");
    org.apache.maven.shared.dependency.graph.DependencyNode parent = createGraphNode("parent", child1, child2);

    when(this.targetFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(false);
    when(this.targetFilter.include(child2.getArtifact())).thenReturn(true);

    assertTrue(this.visitor.visit(parent));

    // Don't process any further children of child2
    assertFalse(this.visitor.visit(child1));
    assertTrue(this.visitor.endVisit(child1));

    assertTrue(this.visitor.visit(child2));
    assertTrue(this.visitor.endVisit(child2));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\""}));
  }

  @Test
  public void defaultArtifactFilter() {
    this.visitor = new GraphBuildingVisitor(this.graphBuilder, this.targetFilter);

    // Use other test (I know this is ugly...)
    parentAndChild();
  }

  private static org.apache.maven.shared.dependency.graph.DependencyNode createGraphNode(String artifactId, org.apache.maven.shared.dependency.graph.DependencyNode... children) {
    org.apache.maven.shared.dependency.graph.DependencyNode node = mock(org.apache.maven.shared.dependency.graph.DependencyNode.class);
    when(node.getArtifact()).thenReturn(createArtifact(artifactId));
    when(node.getChildren()).thenReturn(Arrays.asList(children));
    return node;
  }

  private static Artifact createArtifact(String artifactId) {
    return new DefaultArtifact("groupId", artifactId, "version", "compile", "jar", "", null);
  }

}
