package com.github.ferstl.depgraph.dependency;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.NodeIdRenderers.GROUP_ID;
import static com.github.ferstl.depgraph.dependency.NodeIdRenderers.VERSIONLESS_ID;
import static com.github.ferstl.depgraph.dependency.NodeIdRenderers.VERSIONLESS_ID_WITH_SCOPE;
import static org.junit.Assert.assertEquals;

public class NodeIdRenderersTest {

  @Test
  public void groupId() {
    // act
    String result = GROUP_ID.render(createDependencyNode());

    // assert
    assertEquals("groupId", result);
  }

  @Test
  public void versionLessId() {
    // act
    String result = VERSIONLESS_ID.render(createDependencyNode());

    // assert
    assertEquals("groupId:artifactId:type:classifier", result);
  }

  @Test
  public void versionLessIdWithScope() {
    // act
    String result = VERSIONLESS_ID_WITH_SCOPE.render(createDependencyNode());

    // assert
    assertEquals("groupId:artifactId:type:classifier:compile", result);
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
