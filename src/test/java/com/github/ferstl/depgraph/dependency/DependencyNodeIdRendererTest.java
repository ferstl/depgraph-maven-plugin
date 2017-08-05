package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DependencyNodeIdRendererTest {

  @Test
  public void groupId() {
    // act
    String result = DependencyNodeIdRenderer.groupId().render(createDependencyNode());

    // assert
    assertEquals("groupId", result);
  }

  @Test
  public void versionLessId() {
    // act
    String result = DependencyNodeIdRenderer.versionlessId().render(createDependencyNode());

    // assert
    assertEquals("groupId:artifactId:classifier", result);
  }

  @Test
  public void versionLessIdWithScope() {
    // act
    String result = DependencyNodeIdRenderer.versionlessId().withScope(true).render(createDependencyNode());

    // assert
    assertEquals("groupId:artifactId:classifier:compile", result);
  }


  private static DependencyNode createDependencyNode() {
    Artifact artifact = new DefaultArtifact(
        "groupId",
        "artifactId",
        "1.0.0",
        "compile",
        "type",
        "classifier",
        null);

    return new DependencyNode(artifact);
  }
}
