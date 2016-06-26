package com.github.ferstl.depgraph.graph.style;

import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.graph.NodeResolution;
import com.github.ferstl.depgraph.graph.style.resource.ClasspathStyleResource;
import static org.junit.Assert.assertEquals;


public class StyleConfigurationTest {

  private ClasspathStyleResource testStyle;
  private StyleConfiguration emptyConfig;
  private ClasspathStyleResource testOverride;


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
    assertEquals("", config.edgeAttributes(NodeResolution.INCLUDED).toString());
    assertEquals("[style=\"dashed\"]", config.edgeAttributes(NodeResolution.OMITTED_FOR_DUPLICATE).toString());
    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>compile>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "compile", "compile").toString());
    assertEquals("[shape=\"box\",style=\"filled\",fillcolor=\"orange\",label=<groupId<br/>artifactId<br/>1.0.0<br/>test>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "test", "test").toString());
  }

  @Test
  public void loadWithOverride() {
    StyleConfiguration config = StyleConfiguration.load(this.testStyle, this.testOverride);

    // the scoped nodes in the main config wil be completely replaced
    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>test>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "test", "test").toString());
    assertEquals("[shape=\"box\",color=\"blue\",label=<groupId<br/>artifactId<br/>1.0.0<br/>provided>]", config.nodeAttributes("groupId", "artifactId", "1.0.0", "provided", "provided").toString());
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
    AttributeBuilder attributes = this.emptyConfig.nodeAttributes("groupId", "artifactId", "1.0.0", "compile", "compile");

    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>compile>]", attributes.toString());
  }


  @Test
  public void edgeAttributesForEmptyConfiguration() {
    AttributeBuilder attributes = this.emptyConfig.edgeAttributes(NodeResolution.INCLUDED);

    assertEquals("", attributes.toString());
  }
}
