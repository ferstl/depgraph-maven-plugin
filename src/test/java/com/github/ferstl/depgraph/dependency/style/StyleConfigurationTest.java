/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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
package com.github.ferstl.depgraph.dependency.style;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.dependency.style.resource.ClasspathStyleResource;
import com.github.ferstl.depgraph.dependency.style.resource.FileSystemStyleResource;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.google.common.io.Files;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;


public class StyleConfigurationTest {

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
    assertEquals("", config.edgeAttributes(NodeResolution.INCLUDED, "compile").toString());
    assertEquals("[style=\"dotted\",color=\"gray\"]", config.edgeAttributes(NodeResolution.INCLUDED, "test").toString());
    assertEquals("[style=\"dashed\"]", config.edgeAttributes(NodeResolution.OMITTED_FOR_DUPLICATE, "compile").toString());
    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>compile>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "jar", "compile", "compile").toString());
    assertEquals("[shape=\"box\",style=\"filled\",fillcolor=\"orange\",label=<groupId<br/>artifactId<br/>1.0.0<br/>test>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "jar", "test", "test").toString());
  }

  @Test
  public void loadWithOverride() {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle, this.testOverride);

    assertEquals("[shape=\"ellipse\",color=\"black\",fontname=\"Courier\",fontsize=\"14\",fontcolor=\"green\"]", config.defaultNodeAttributes().toString());
    assertEquals("[style=\"dashed\",color=\"blue\"]", config.defaultEdgeAttributes().toString());
    assertEquals("[style=\"dotted\",color=\"blue\"]", config.edgeAttributes(NodeResolution.INCLUDED, "test").toString());
    assertEquals("[style=\"dashed\",color=\"green\"]", config.edgeAttributes(NodeResolution.OMITTED_FOR_DUPLICATE, "test").toString());
    assertEquals("[fontname=\"Courier\"]", config.edgeAttributes(NodeResolution.OMITTED_FOR_CONFLICT, "provided").toString());

    assertEquals("[shape=\"box\",style=\"filled\",color=\"red\",fillcolor=\"orange\",label=<groupId<br/>artifactId<br/>1.0.0<br/>test>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "jar", "test", "test").toString());
    assertEquals("[shape=\"box\",color=\"blue\",label=<groupId<br/>artifactId<br/>1.0.0<br/>provided>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "jar", "provided", "provided").toString());
  }

  @Test
  public void defaultNodeAttributesForEmptyConfiguration() {
    AttributeBuilder attributes = this.emptyConfig.defaultNodeAttributes();

    assertEquals("[shape=\"box\"]", attributes.toString());
  }

  @Test
  public void defaultEdgeAttributesForEmptyConfiguration() {
    AttributeBuilder attributes = this.emptyConfig.defaultEdgeAttributes();

    assertEquals("", attributes.toString());
  }

  @Test
  public void nodeAttributesForEmptyConfiguration() {
    AttributeBuilder attributes = this.emptyConfig.nodeAttributes("groupId", "artifactId", "1.0.0", "jar", "compile", "compile");

    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>compile>]", attributes.toString());
  }


  @Test
  public void edgeAttributesForEmptyConfiguration() {
    AttributeBuilder attributes = this.emptyConfig.edgeAttributes(NodeResolution.INCLUDED, "compile");

    assertEquals("", attributes.toString());
  }

  @Test
  public void toJson() throws IOException {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle, this.testOverride);

    String json = config.toJson();
    File configFile = this.tmp.newFile("config.json");
    Files.write(json, configFile, UTF_8);

    StyleConfiguration reloadedConfig = StyleConfiguration.load(new FileSystemStyleResource(configFile.toPath()));
    assertEquals("[fontname=\"Courier\"]", reloadedConfig.edgeAttributes(NodeResolution.OMITTED_FOR_CONFLICT, "provided").toString());
  }
}
