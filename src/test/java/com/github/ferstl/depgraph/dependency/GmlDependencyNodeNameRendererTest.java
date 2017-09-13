/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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

import org.junit.Test;

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public class GmlDependencyNodeNameRendererTest {

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, false, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("", result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"groupId\"", result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, true, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"artifactId\"", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, false, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("label \"version\"", result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, true, false, false, true);

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
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, true, false, false, false);

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
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(true, false, false, false, true);

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
    GmlDependencyNodeNameRenderer renderer = new GmlDependencyNodeNameRenderer(false, true, false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "label \"artifactId\n"
        + "version\"";

    assertEquals(expected, result);
  }
}
