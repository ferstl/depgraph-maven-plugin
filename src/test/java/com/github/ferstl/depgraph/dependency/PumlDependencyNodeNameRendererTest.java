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

import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addClassifiers;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.addTypes;
import static com.github.ferstl.depgraph.dependency.DependencyNodeUtil.createDependencyNode;
import static org.junit.Assert.assertEquals;

public class PumlDependencyNodeNameRendererTest {

  @Test
  public void renderNothing() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderGroupId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, false, false, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"groupId\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    assertEquals("{\"component\":\"rectangle\",\"label\":\"artifactId\",\"stereotype\":\"compile\"}", result);
  }

  @Test
  public void renderVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, false, false, false,true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"version\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, true,false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"groupId:artifactId:version\",\"stereotype\":\"compile\"}";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdArtifactId() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, true, false, false,false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"groupId:artifactId\",\"stereotype\":\"compile\"}";

    assertEquals(expected, result);
  }

  @Test
  public void renderGroupIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, false, false, false,true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"groupId:version\",\"stereotype\":\"compile\"}";

    assertEquals(expected, result);
  }

  @Test
  public void renderArtifactIdVersion() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true,false, false, true);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"artifactId:version\",\"stereotype\":\"compile\"}";

    assertEquals(expected, result);
  }


  @Test
  public void renderTypes() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar", "zip");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"artifactId:.jar/.zip\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderJarTypeOnly() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addTypes(node, "jar");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true, true, false, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"artifactId:\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderClassifiers() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "classifier1", "classifier2");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"artifactId:classifier1/classifier2\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderEmptyClassifier() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version");
    addClassifiers(node, "");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(false, true, false, true, false);

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"artifactId:\",\"stereotype\":\"compile\"}";
    assertEquals(expected, result);
  }

  @Test
  public void renderAll() {
    // arrange
    DependencyNode node = createDependencyNode("groupId", "artifactId", "version", "test");
    PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, true, true, true, true);
    addClassifiers(node, "classifier1", "classifier2");
    addTypes(node, "jar", "zip", "tar.gz");

    // act
    String result = renderer.render(node);

    // assert
    String expected = "{\"component\":\"rectangle\",\"label\":\"groupId:artifactId:.jar/.tar.gz/.zip:classifier1/classifier2:version\",\"stereotype\":\"test\"}";
    assertEquals(expected, result);
  }
}
