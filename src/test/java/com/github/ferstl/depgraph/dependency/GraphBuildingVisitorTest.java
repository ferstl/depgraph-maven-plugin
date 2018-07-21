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

import java.util.Arrays;
import java.util.EnumSet;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
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
  private ArtifactFilter globalFilter;
  private ArtifactFilter transitiveFilter;
  private ArtifactFilter targetFilter;
  private EnumSet<NodeResolution> includedResolutions;

  @Before
  public void before() {
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);

    this.globalFilter = mock(ArtifactFilter.class);
    when(this.globalFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    this.transitiveFilter = mock(ArtifactFilter.class);
    when(this.transitiveFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    // this is the same as an empty list of target dependencies
    this.targetFilter = mock(ArtifactFilter.class);
    when(this.targetFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    this.includedResolutions = allOf(NodeResolution.class);
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

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, false);
    assertTrue(visitor.visit(parent));
    assertTrue(visitor.visit(child));
    assertTrue(visitor.endVisit(child));
    assertTrue(visitor.endVisit(parent));

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

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, this.includedResolutions);
    assertTrue(visitor.visit(parent));
    assertTrue(visitor.visit(child1));
    assertTrue(visitor.endVisit(child1));

    // Don't process any further children of child2
    assertFalse(visitor.visit(child2));
    assertTrue(visitor.endVisit(child2));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child1:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]"},
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

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, false);
    assertTrue(visitor.visit(parent));

    assertTrue(visitor.visit(child1));
    assertTrue(visitor.endVisit(child1));

    assertTrue(visitor.visit(child2));
    assertTrue(visitor.endVisit(child2));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child2:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\""}));
  }

  /**
   * .
   * <pre>
   * parent
   * - child1
   *   - child4 (target dependency)
   * - child2
   * - child3 (target dependency)
   *   - child4 (target dependency)
   * </pre>
   */
  @Test
  public void multiTargetDepNode() {
    org.apache.maven.shared.dependency.graph.DependencyNode child4 = createGraphNode("child4");
    org.apache.maven.shared.dependency.graph.DependencyNode child1 = createGraphNode("child1", child4);
    org.apache.maven.shared.dependency.graph.DependencyNode child2 = createGraphNode("child2");
    org.apache.maven.shared.dependency.graph.DependencyNode child3 = createGraphNode("child3", child4);
    org.apache.maven.shared.dependency.graph.DependencyNode parent = createGraphNode("parent", child1, child2, child3);

    when(this.targetFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(false);
    when(this.targetFilter.include(child3.getArtifact())).thenReturn(true);
    when(this.targetFilter.include(child4.getArtifact())).thenReturn(true);

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, false);
    assertTrue(visitor.visit(parent));

    assertTrue(visitor.visit(child1));
    assertTrue(visitor.visit(child4));
    assertTrue(visitor.endVisit(child4));
    assertTrue(visitor.endVisit(child1));

    assertTrue(visitor.visit(child2));
    assertTrue(visitor.endVisit(child2));

    assertTrue(visitor.visit(child3));
    assertTrue(visitor.visit(child4));
    assertTrue(visitor.endVisit(child4));
    assertTrue(visitor.endVisit(child3));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child1:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]",
            "\"groupId:child3:jar:version:compile\"[label=\"groupId:child3:jar:version:compile\"]",
            "\"groupId:child4:jar:version:compile\"[label=\"groupId:child4:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child3:jar:version:compile\"",
            "\"groupId:child1:jar:version:compile\" -> \"groupId:child4:jar:version:compile\"",
            "\"groupId:child3:jar:version:compile\" -> \"groupId:child4:jar:version:compile\""


        }));
  }

  /**
   * .
   * <pre>
   * node-a
   *   - node-b
   *   - node-c
   *   - node-d (omitted)
   *   - node-test (not omitted test dependency)
   * node-b
   *   - node-d
   *   - node-test
   * node-c
   *   - node-d
   * </pre>
   */
  @Test
  public void omitReachablePaths() {
    org.apache.maven.shared.dependency.graph.DependencyNode nodeTest = createGraphNode("node-test");
    nodeTest.getArtifact().setScope("test");

    org.apache.maven.shared.dependency.graph.DependencyNode nodeD = createGraphNode("node-d");
    org.apache.maven.shared.dependency.graph.DependencyNode nodeC = createGraphNode("node-c", nodeD);
    org.apache.maven.shared.dependency.graph.DependencyNode nodeB = createGraphNode("node-b", nodeD, nodeTest);
    org.apache.maven.shared.dependency.graph.DependencyNode nodeA = createGraphNode("node-a", nodeB, nodeC, nodeD, nodeTest);

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, true);

    assertTrue(visitor.visit(nodeA));

    assertTrue(visitor.visit(nodeB));
    assertTrue(visitor.visit(nodeD));
    assertTrue(visitor.endVisit(nodeD));
    assertTrue(visitor.visit(nodeTest));
    assertTrue(visitor.endVisit(nodeTest));
    assertTrue(visitor.endVisit(nodeB));

    assertTrue(visitor.visit(nodeC));
    assertTrue(visitor.visit(nodeD));
    assertTrue(visitor.endVisit(nodeD));
    assertTrue(visitor.endVisit(nodeC));

    assertTrue(visitor.visit(nodeD));
    assertTrue(visitor.endVisit(nodeD));

    assertTrue(visitor.visit(nodeTest));
    assertTrue(visitor.endVisit(nodeTest));

    assertTrue(visitor.endVisit(nodeA));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:node-b:jar:version:compile\"",
            "\"groupId:node-d:jar:version:compile\"",
            "\"groupId:node-test:jar:version:test\"",
            "\"groupId:node-a:jar:version:compile\"",
            "\"groupId:node-c:jar:version:compile\""
        },
        new String[]{
            "\"groupId:node-b:jar:version:compile\" -> \"groupId:node-d:jar:version:compile\"",
            "\"groupId:node-b:jar:version:compile\" -> \"groupId:node-test:jar:version:test\"",
            "\"groupId:node-a:jar:version:compile\" -> \"groupId:node-b:jar:version:compile\"",
            "\"groupId:node-c:jar:version:compile\" -> \"groupId:node-d:jar:version:compile\"",
            "\"groupId:node-a:jar:version:compile\" -> \"groupId:node-c:jar:version:compile\"",
            "\"groupId:node-a:jar:version:compile\" -> \"groupId:node-test:jar:version:test\"",
        }
    ));
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
