package com.github.ferstl.depgraph.graph.style;

import org.junit.Test;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.graph.NodeResolution;
import static org.junit.Assert.assertEquals;


public class StyleConfigurationTest {

  @Test
  public void defaultNodeAttributesForEmptyConfiguration() {
    StyleConfiguration emptyConfig = new StyleConfiguration();
    AttributeBuilder attributes = emptyConfig.defaultNodeAttributes();

    assertEquals("[shape=\"box\"]", attributes.toString());
  }

  @Test
  public void defaultEdgeAttributesForEmptyConfiguration() {
    StyleConfiguration emptyConfig = new StyleConfiguration();
    AttributeBuilder attributes = emptyConfig.defaultEdgeAttributes();

    assertEquals("", attributes.toString());
  }

  @Test
  public void nodeAttributesForEmptyConfiguration() {
    StyleConfiguration emptyConfig = new StyleConfiguration();
    AttributeBuilder attributes = emptyConfig.nodeAttributes("groupId", "artifactId", "1.0.0", "compile", "compile");

    assertEquals("[label=<groupId<br/>artifactId<br/>1.0.0<br/>compile>]", attributes.toString());
  }


  @Test
  public void edgeAttributesForEmptyConfiguration() {
    StyleConfiguration emptyConfig = new StyleConfiguration();
    AttributeBuilder attributes = emptyConfig.edgeAttributes(NodeResolution.INCLUDED);

    assertEquals("", attributes.toString());
  }
}
