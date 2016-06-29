package com.github.ferstl.depgraph.graph.style;

import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import static org.junit.Assert.assertEquals;


public class FontTest {

  private Font font;
  private Font emptyFont;

  @Before
  public void before() {
    this.font = new Font();
    this.font.color = "blue";
    this.font.size = 10;
    this.font.name = "Courier";

    this.emptyFont = new Font();
  }

  @Test
  public void setAttributes() {
    // arrange
    AttributeBuilder builder = new AttributeBuilder();

    // act
    this.font.setAttributes(builder);

    // assert
    assertEquals("[fontcolor=\"blue\",fontsize=\"10\",fontname=\"Courier\"]", builder.toString());
  }

  @Test
  public void setAttributesEmpty() {
    AttributeBuilder builder = new AttributeBuilder();

    // act
    this.emptyFont.setAttributes(builder);

    // assert
    assertEquals("", builder.toString());
  }

  @Test
  public void mergeFullToFull() {
    // arrange
    Font other = new Font();
    other.color = "red";
    other.size = 12;
    other.name = "Sans";

    // act
    this.font.merge(other);

    // assert
    assertEquals("red", this.font.color);
    assertEquals((Integer) 12, this.font.size);
    assertEquals("Sans", this.font.name);
  }

  @Test
  public void mergeEmptyToFull() {
    // act
    this.font.merge(this.emptyFont);

    // assert
    assertEquals("blue", this.font.color);
    assertEquals((Integer) 10, this.font.size);
    assertEquals("Courier", this.font.name);
  }

  @Test
  public void mergeFullToEmpty() {
    // act
    this.emptyFont.merge(this.font);

    // assert
    assertEquals("blue", this.font.color);
    assertEquals((Integer) 10, this.font.size);
    assertEquals("Courier", this.font.name);
  }

}
