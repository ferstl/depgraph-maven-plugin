/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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

import java.util.EnumSet;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.resolver.filter.IncludesArtifactFilter;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static com.github.ferstl.depgraph.graph.GraphBuilderMatcher.hasNodesAndEdges;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GraphBuildingVisitorTest {

  private GraphBuilder<DependencyNode> graphBuilder;
  private ArtifactFilter globalFilter;
  private ArtifactFilter transitiveFilter;
  private ArtifactFilter targetFilter;
  private EnumSet<NodeResolution> includedResolutions;

  @BeforeEach
  void before() {
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);

    this.globalFilter = mock(ArtifactFilter.class);
    when(this.globalFilter.include(any())).thenReturn(true);

    this.transitiveFilter = mock(ArtifactFilter.class);
    when(this.transitiveFilter.include(any())).thenReturn(true);

    // this is the same as an empty list of target dependencies
    this.targetFilter = mock(ArtifactFilter.class);
    when(this.targetFilter.include(any())).thenReturn(true);

    this.includedResolutions = allOf(NodeResolution.class);
  }

  /**
   * .
   * <pre>
   * parent
   *     - child1
   *     - child2 (test)
   * </pre>
   */
  @Test
  void parentAndChildren() {
    org.eclipse.aether.graph.DependencyNode child1 = createMavenDependencyNode("child1");
    org.eclipse.aether.graph.DependencyNode child2 = createMavenDependencyNode("child2", "test");
    org.eclipse.aether.graph.DependencyNode parent = createMavenDependencyNode("parent", child1, child2);

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, this.includedResolutions);
    assertTrue(visitor.visitEnter(parent));
    assertTrue(visitor.visitEnter(child1));
    assertTrue(visitor.visitLeave(child1));
    assertTrue(visitor.visitEnter(child2));
    assertTrue(visitor.visitLeave(child2));
    assertTrue(visitor.visitLeave(parent));

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:test\""}));
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
  void ignoredNode() {
    org.eclipse.aether.graph.DependencyNode child1 = createMavenDependencyNode("child1");
    org.eclipse.aether.graph.DependencyNode child2 = createMavenDependencyNode("child2");
    org.eclipse.aether.graph.DependencyNode parent = createMavenDependencyNode("parent", child1, child2);

    String filterPattern = child2.getArtifact().getGroupId() + ":" + child2.getArtifact().getArtifactId();
    this.globalFilter = new ExcludesArtifactFilter(singletonList(filterPattern));

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, this.includedResolutions);
    assertTrue(visitor.visitEnter(parent));
    assertTrue(visitor.visitEnter(child1));
    assertTrue(visitor.visitLeave(child1));

    // Don't process any further children of child2
    assertTrue(visitor.visitEnter(child2));
    assertTrue(visitor.visitLeave(child2));

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
  void targetDepNode() {
    org.eclipse.aether.graph.DependencyNode child1 = createMavenDependencyNode("child1");
    org.eclipse.aether.graph.DependencyNode child2 = createMavenDependencyNode("child2");
    org.eclipse.aether.graph.DependencyNode parent = createMavenDependencyNode("parent", child1, child2);

    String filterPattern = child2.getArtifact().getGroupId() + ":" + child2.getArtifact().getArtifactId();
    this.targetFilter = new IncludesArtifactFilter(singletonList(filterPattern));

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, this.includedResolutions);
    assertTrue(visitor.visitEnter(parent));

    assertTrue(visitor.visitEnter(child1));
    assertTrue(visitor.visitLeave(child1));

    assertTrue(visitor.visitEnter(child2));
    assertTrue(visitor.visitLeave(child2));

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
  void multiTargetDepNode() {
    org.eclipse.aether.graph.DependencyNode child4 = createMavenDependencyNode("child4");
    org.eclipse.aether.graph.DependencyNode child1 = createMavenDependencyNode("child1", child4);
    org.eclipse.aether.graph.DependencyNode child2 = createMavenDependencyNode("child2");
    org.eclipse.aether.graph.DependencyNode child3 = createMavenDependencyNode("child3", child4);
    org.eclipse.aether.graph.DependencyNode parent = createMavenDependencyNode("parent", child1, child2, child3);

    String child3Pattern = child3.getArtifact().getGroupId() + ":" + child3.getArtifact().getArtifactId();
    String child4Pattern = child4.getArtifact().getGroupId() + ":" + child4.getArtifact().getArtifactId();
    this.targetFilter = new IncludesArtifactFilter(asList(child3Pattern, child4Pattern));

    GraphBuildingVisitor visitor = new GraphBuildingVisitor(this.graphBuilder, this.globalFilter, this.transitiveFilter, this.targetFilter, this.includedResolutions);
    assertTrue(visitor.visitEnter(parent));

    assertTrue(visitor.visitEnter(child1));
    assertTrue(visitor.visitEnter(child4));
    assertTrue(visitor.visitLeave(child4));
    assertTrue(visitor.visitLeave(child1));

    assertTrue(visitor.visitEnter(child2));
    assertTrue(visitor.visitLeave(child2));

    assertTrue(visitor.visitEnter(child3));
    assertTrue(visitor.visitEnter(child4));
    assertTrue(visitor.visitLeave(child4));
    assertTrue(visitor.visitLeave(child3));

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


  private static org.eclipse.aether.graph.DependencyNode createMavenDependencyNode(String artifactId, org.eclipse.aether.graph.DependencyNode... children) {
    return createMavenDependencyNode(artifactId, "compile", children);
  }

  private static org.eclipse.aether.graph.DependencyNode createMavenDependencyNode(String artifactId, String scope, org.eclipse.aether.graph.DependencyNode... children) {
    Dependency dependency = new Dependency(createArtifact(artifactId), scope);
    DefaultDependencyNode node = new DefaultDependencyNode(dependency);
    node.setChildren(asList(children));

    return node;
  }

  private static org.eclipse.aether.artifact.Artifact createArtifact(String artifactId) {
    return new org.eclipse.aether.artifact.DefaultArtifact("groupId", artifactId, "", "jar", "version");
  }

}
