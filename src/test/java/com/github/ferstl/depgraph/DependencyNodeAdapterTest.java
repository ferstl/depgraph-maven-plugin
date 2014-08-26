package com.github.ferstl.depgraph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link DependencyNodeAdapter}.
 */
public class DependencyNodeAdapterTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void defaultResolutionFromArtifact() {
    DependencyNodeAdapter adapter = new DependencyNodeAdapter(createArtifact());
    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultResolutionFromDependencyGraphNode() {
    org.apache.maven.shared.dependency.graph.DependencyNode node =
        new org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode(null, createArtifact(), "", "", "");
    DependencyNodeAdapter adapter = new DependencyNodeAdapter(node);

    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultResolutionFromDependencyTreeNode() {
    org.apache.maven.shared.dependency.tree.DependencyNode node =
        new org.apache.maven.shared.dependency.tree.DependencyNode(createArtifact());
    DependencyNodeAdapter adapter = new DependencyNodeAdapter(node);

    assertEquals(NodeResolution.INCLUDED, adapter.getResolution());
  }

  @Test
  public void defaultCompileScope() {
    Artifact artifact = createArtifact();
    artifact.setScope(null);

    DependencyNodeAdapter adapter = new DependencyNodeAdapter(artifact);
    assertEquals("compile", adapter.getArtifact().getScope());
  }

  @Test
  public void nullArtifact() {
    this.expectedException.expect(NullPointerException.class);
    this.expectedException.expectMessage(not(isEmptyString()));

    new DependencyNodeAdapter((Artifact) null);
  }


  private Artifact createArtifact() {
    return new DefaultArtifact("groupId", "artifactId", "1.0.0", "compile", "jar", "", null);
  }

}
