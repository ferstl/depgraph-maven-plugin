package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addClassifiers;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addTypes;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.*;

public class JsonDependencyNodeNameRendererTest {

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("{\"scopes\":[\"compile\"]}", result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(true, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("{\"groupId\":\"groupId\",\"scopes\":[\"compile\"]}", result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, false, false, false,true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("{\"version\":\"version\",\"scopes\":[\"compile\"]}", result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(true, true,false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(true, true, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(true, false, false, false,true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"groupId\":\"groupId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";

    assertEquals(expected, result);
  }

  @Test
  public void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true,false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"artifactId\":\"artifactId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";

    assertEquals(expected, result);
  }


  @Test
  public void renderTypes() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar", "zip");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"],\"types\":[\"jar\",\"zip\"]}";
    assertEquals(expected, result);
  }

  @Test
  public void renderJarTypeOnly() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"],\"types\":[\"jar\"]}";
    assertEquals(expected, result);
  }

  @Test
  public void renderClassifiers() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "classifier1", "classifier2");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"artifactId\":\"artifactId\",\"classifiers\":[\"classifier1\",\"classifier2\"],\"scopes\":[\"compile\"]}";
    assertEquals(expected, result);
  }

  @Test
  public void renderEmptyClassifier() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}";
    assertEquals(expected, result);
  }

  @Test
  public void renderAll() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", "test");
    JsonDependencyNodeNameRenderer renderer = new JsonDependencyNodeNameRenderer(true, true, true, true, true);
    addClassifiers(node, "classifier1", "classifier2");
    addTypes(node, "jar", "zip", "tar.gz");

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"version\":\"version\",\"classifiers\":[\"classifier1\",\"classifier2\"],\"scopes\":[\"test\"],\"types\":[\"jar\",\"tar.gz\",\"zip\"]}";
    assertEquals(expected, result);
  }
}
