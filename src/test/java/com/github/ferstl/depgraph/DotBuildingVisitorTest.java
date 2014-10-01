package com.github.ferstl.depgraph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.ferstl.depgraph.dot.DotBuilder;

import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodesAndEdges;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DotBuildingVisitorTest {

  private DotBuilder dotBuilder;
  private DotBuildingVisitor visitor;
  private ArtifactFilter artifactFilter;

  @Before
  public void before() {
    this.dotBuilder = new DotBuilder();
    this.artifactFilter = mock(ArtifactFilter.class);
    when(this.artifactFilter.include(Mockito.<Artifact>any())).thenReturn(true);

    this.visitor = new DotBuildingVisitor(this.dotBuilder, this.artifactFilter);
  }

  /**
   * <pre>
   * parent
   * - child
   * </pre>
   */
  @Test
  public void parentAndChild() {
    DependencyNode parent = createGraphNode("parent");
    DependencyNode child = createGraphNode("child");

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
    DependencyNode parent = createGraphNode("parent");
    DependencyNode child1 = createGraphNode("child1");
    DependencyNode child2 = createGraphNode("child2");

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

  @Test
  public void defaultArtifactFilter() {
    this.visitor = new DotBuildingVisitor(this.dotBuilder);

    // Use other test (I know this is ugly...)
    parentAndChild();
  }

  private static org.apache.maven.shared.dependency.graph.DependencyNode createGraphNode(String artifactId) {
    DependencyNode node = mock(org.apache.maven.shared.dependency.graph.DependencyNode.class);
    when(node.getArtifact()).thenReturn(createArtifact(artifactId));

    return node;
  }

  private static Artifact createArtifact(String artifactId) {
    return new DefaultArtifact("groupId", artifactId, "version", "compile", "jar", "", null);
  }

}
