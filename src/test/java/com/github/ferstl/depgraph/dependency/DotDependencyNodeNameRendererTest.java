package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.dependency.style.StyleConfiguration;
import org.junit.Before;
import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public class DotDependencyNodeNameRendererTest {

  private StyleConfiguration styleConfiguration;

  @Before
  public void before() {
    this.styleConfiguration = new StyleConfiguration();
  }

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(false, false, false, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=\"\"]", result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(true, false, false, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<groupId>]", result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(false, true, false, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<artifactId>]", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(false, false, true, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<version>]", result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(true, true, true, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<groupId<br/>artifactId<br/>version>]", result);
  }

  @Test
  public void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(true, true, false, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<groupId<br/>artifactId>]", result);
  }

  @Test
  public void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(true, false, true, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<groupId<br/>version>]", result);
  }

  @Test
  public void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    DotDependencyNodeNameRenderer renderer = new DotDependencyNodeNameRenderer(false, true, true, this.styleConfiguration);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("[label=<artifactId<br/>version>]", result);
  }
}
