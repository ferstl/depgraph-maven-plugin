package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public class SimpleDependencyNodeNameRendererTest {

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("", result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("groupId", result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("artifactId", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("version", result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(true, true, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "groupId\n"
        + "artifactId\n"
        + "version";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(true, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "groupId\n"
        + "artifactId";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(true, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "groupId\n"
        + "version";

    assertEquals(expected, result);
  }

  @Test
  public void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    SimpleDependencyNodeNameRenderer renderer = new SimpleDependencyNodeNameRenderer(false, true, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "artifactId\n"
        + "version";

    assertEquals(expected, result);
  }
}
