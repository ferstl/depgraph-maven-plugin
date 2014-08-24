package com.github.ferstl.depgraph.dot;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Before;
import org.junit.Test;

import com.github.ferstl.depgraph.DependencyNodeAdapter;

import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.contains;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for {@link DotBuilder}.
 */
public class DotBuilderTest {

  private DotBuilder dotBuilder;

  @Before
  public void before() {
    this.dotBuilder = new DotBuilder();
  }

  @Test
  public void defaults() {
    Node from = createNode("from");
    Node to = createNode("to");

    this.dotBuilder.addEdge(from, to);

    assertThat(this.dotBuilder, contains(
        new String[] {
          "\"group:from:jar:1.0.0:compile\"[label=\"group:from:jar:1.0.0:compile\"]",
          "\"group:to:jar:1.0.0:compile\"[label=\"group:to:jar:1.0.0:compile\"]"},
        new String[] {
          "\"group:from:jar:1.0.0:compile\" -> \"group:to:jar:1.0.0:compile\""}));

  }



  private Node createNode(String name) {
    Artifact artifact = new DefaultArtifact("group", name, "1.0.0", "compile", "jar", "", null);

    return new DependencyNodeAdapter(artifact);
  }

}
