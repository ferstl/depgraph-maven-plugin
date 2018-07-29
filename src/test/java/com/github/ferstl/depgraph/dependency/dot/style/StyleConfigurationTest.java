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
package com.github.ferstl.depgraph.dependency.dot.style;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.dependency.dot.style.resource.ClasspathStyleResource;
import com.github.ferstl.depgraph.dependency.dot.style.resource.FileSystemStyleResource;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;
import com.google.common.io.Files;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;


public class StyleConfigurationTest {

  private static final String SCOPE_COMPILE = "compile";
  private static final String SCOPE_PROVIDED = "provided";
  private static final String SCOPE_TEST = "test";
  private static final String GROUP_ID = "groupId";
  private static final String ARTIFACT_ID = "artifactId";
  private static final String VERSION = "1.0.0";
  private static final String TYPE = "jar";
  private static final String CLASSIFIER = "classifier";
  private static final String CLASSIFIER_DEFAULT = "";
  private static final String CLASSIFIER_LINUX = "linux";

  private static final StyleKey COMPILE_STYLE_KEY = StyleKey.create(GROUP_ID, ARTIFACT_ID, SCOPE_COMPILE, TYPE, VERSION, CLASSIFIER_DEFAULT, null);
  private static final StyleKey TEST_STYLE_KEY = StyleKey.create(GROUP_ID, ARTIFACT_ID, SCOPE_TEST, TYPE, VERSION, CLASSIFIER_LINUX, null);
  private static final StyleKey TEST_STYLE_KEY_CLASSIFIER = StyleKey.create(GROUP_ID, ARTIFACT_ID, SCOPE_TEST, TYPE, VERSION, CLASSIFIER, null);
  private static final StyleKey PROVIDED_STYLE_KEY = StyleKey.create(GROUP_ID, ARTIFACT_ID, SCOPE_PROVIDED, TYPE, VERSION, CLASSIFIER_DEFAULT, null);
  private static final StyleKey OPTIOINAL_STYLE_KEY = StyleKey.create(GROUP_ID, ARTIFACT_ID, SCOPE_COMPILE, TYPE, VERSION, CLASSIFIER_DEFAULT, true);

  private ClasspathStyleResource testStyle;
  private StyleConfiguration emptyConfig;
  private ClasspathStyleResource testOverride;

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder(Paths.get("target").toFile());


  @Before
  public void before() {
    this.emptyConfig = new StyleConfiguration();
    this.testStyle = new ClasspathStyleResource("test-style.json", getClass().getClassLoader());
    this.testOverride = new ClasspathStyleResource("test-override-style.json", getClass().getClassLoader());
  }

  @Test
  public void load() {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle);

    assertEquals("[shape=\"polygon\",color=\"black\",fontname=\"Courier\",fontsize=\"14\",fontcolor=\"green\",sides=\"8\"]", config.defaultNodeAttributes().toString());
    assertEquals("[style=\"dotted\",color=\"blue\"]", config.defaultEdgeAttributes().toString());
    assertEquals("", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.INCLUDED, SCOPE_COMPILE, null, null).toString());
    assertEquals("[style=\"dotted\",color=\"gray\"]", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.INCLUDED, SCOPE_TEST, null, null).toString());
    assertEquals("[style=\"dashed\"]", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.OMITTED_FOR_DUPLICATE, SCOPE_COMPILE, null, null).toString());
    assertEquals("[style=\"dotted\"]", config.edgeAttributes(NodeResolution.PARENT, NodeResolution.INCLUDED, SCOPE_COMPILE, null, null).toString());

    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>compile>]", config.nodeAttributes(COMPILE_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER_DEFAULT, SCOPE_COMPILE).toString());
    assertEquals("[label=<<font point-size=\"9\">&lt;optional&gt;</font><br/>groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>compile>]", config.nodeAttributes(COMPILE_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, true, TYPE, CLASSIFIER_DEFAULT, SCOPE_COMPILE).toString());
    assertEquals("[shape=\"box\",style=\"filled\",fillcolor=\"orange\",label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>linux<br/>test>]", config.nodeAttributes(TEST_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER_LINUX, SCOPE_TEST).toString());
    assertEquals("[shape=\"polygon\",style=\"filled\",fillcolor=\"green\",label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>classifier<br/>test>]", config.nodeAttributes(TEST_STYLE_KEY_CLASSIFIER, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER, SCOPE_TEST).toString());
    assertEquals("[shape=\"polygon\",style=\"filled\",fillcolor=\"white\",label=<&lt;optional&gt;<br/>groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>compile>]", config.nodeAttributes(OPTIOINAL_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, true, TYPE, CLASSIFIER_DEFAULT, SCOPE_COMPILE).toString());
  }

  @Test
  public void loadWithOverride() {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle, this.testOverride);

    assertEquals("[rankdir=\"LR\"]", config.graphAttributes().toString());
    assertEquals("[shape=\"ellipse\",color=\"black\",fontname=\"Courier\",fontsize=\"14\",fontcolor=\"green\"]", config.defaultNodeAttributes().toString());
    assertEquals("[style=\"dashed\",color=\"blue\"]", config.defaultEdgeAttributes().toString());
    assertEquals("[style=\"dotted\",color=\"blue\"]", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.INCLUDED, SCOPE_TEST, null, null).toString());
    assertEquals("[style=\"dashed\",color=\"green\"]", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.OMITTED_FOR_DUPLICATE, SCOPE_TEST, null, null).toString());
    assertEquals("[fontname=\"Courier\"]", config.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.OMITTED_FOR_CONFLICT, StyleConfigurationTest.SCOPE_PROVIDED, null, null).toString());

    assertEquals("[shape=\"box\",style=\"filled\",color=\"red\",fillcolor=\"orange\",label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>test>]", config.nodeAttributes(TEST_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER_DEFAULT, SCOPE_TEST).toString());
    assertEquals("[shape=\"box\",color=\"blue\",label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>linux<br/>provided>]", config.nodeAttributes(PROVIDED_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER_LINUX, SCOPE_PROVIDED).toString());
  }

  @Test
  public void defaultNodeAttributesForEmptyConfiguration() {
    DotAttributeBuilder attributes = this.emptyConfig.defaultNodeAttributes();

    assertEquals("[shape=\"box\"]", attributes.toString());
  }

  @Test
  public void defaultEdgeAttributesForEmptyConfiguration() {
    DotAttributeBuilder attributes = this.emptyConfig.defaultEdgeAttributes();

    assertEquals("", attributes.toString());
  }

  @Test
  public void nodeAttributesForEmptyConfiguration() {
    DotAttributeBuilder attributes = this.emptyConfig.nodeAttributes(COMPILE_STYLE_KEY, GROUP_ID, ARTIFACT_ID, VERSION, false, TYPE, CLASSIFIER_LINUX, SCOPE_COMPILE);

    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>jar<br/>linux<br/>compile>]", attributes.toString());
  }


  @Test
  public void edgeAttributesForEmptyConfiguration() {
    DotAttributeBuilder attributes = this.emptyConfig.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.INCLUDED, SCOPE_COMPILE, null, null);

    assertEquals("", attributes.toString());
  }

  @Test
  public void toJson() throws IOException {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle, this.testOverride);

    String json = config.toJson();
    File configFile = this.tmp.newFile("config.json");
    Files.asCharSink(configFile, UTF_8).write(json);

    StyleConfiguration reloadedConfig = StyleConfiguration.load(new FileSystemStyleResource(configFile.toPath()));
    assertEquals("[fontname=\"Courier\"]", reloadedConfig.edgeAttributes(NodeResolution.INCLUDED, NodeResolution.OMITTED_FOR_CONFLICT, SCOPE_PROVIDED, null, null).toString());
  }
}
