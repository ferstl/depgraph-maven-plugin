package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public class GmlDependencyNodeNameRendererTest {

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("", result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"groupId\"", result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"artifactId\"", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"version\"", result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, true, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "label \"groupId\n"
        + "artifactId\n"
        + "version\"";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "label \"groupId\n"
        + "artifactId\"";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "label \"groupId\n"
        + "version\"";

    assertEquals(expected, result);
  }

  @Test
  public void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, true, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "label \"artifactId\n"
        + "version\"";

    assertEquals(expected, result);
  }
}
