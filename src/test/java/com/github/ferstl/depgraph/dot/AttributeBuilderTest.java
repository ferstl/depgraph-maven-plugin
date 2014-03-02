package com.github.ferstl.depgraph.dot;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link AttributeBuilder}.
 */
public class AttributeBuilderTest {


  @Test
  public void empty() {
    assertEquals("", new AttributeBuilder().toString());
  }

  @Test
  public void label() {
    assertEquals("[label=someLabel]", new AttributeBuilder().label("someLabel").toString());
  }

  @Test
  public void fontName() {
    assertEquals("[fontname=Helvetica]", new AttributeBuilder().fontName("Helvetica").toString());
  }

  @Test
  public void fontSize() {
    assertEquals("[fontsize=12]", new AttributeBuilder().fontSize(12).toString());
  }

  @Test
  public void fontColor() {
    assertEquals("[fontcolor=green]", new AttributeBuilder().fontColor("green").toString());
  }

  @Test
  public void style() {
    assertEquals("[style=dashed]", new AttributeBuilder().style("dashed").toString());
  }

  @Test
  public void color() {
    assertEquals("[color=blue]", new AttributeBuilder().color("blue").toString());
  }

  @Test
  public void shape() {
    assertEquals("[shape=box]", new AttributeBuilder().shape("box").toString());
  }

  @Test
  public void addAttribute() {
    assertEquals("[someAttribute=someValue]", new AttributeBuilder().addAttribute("someAttribute", "someValue").toString());
  }

  @Test
  public void multipleAttributes() {
    assertEquals("[label=someLabel,color=green,fontsize=10]", new AttributeBuilder().label("someLabel").color("green").fontSize(10).toString());
  }

  @Test
  public void quoting() {
    assertEquals("[label=\"some Label\"]", new AttributeBuilder().label("some Label").toString());
  }

  @Test
  public void escaping() {
    assertEquals("[label=\"some\\nLabel\"]", new AttributeBuilder().label("some\nLabel").toString());
  }

}
