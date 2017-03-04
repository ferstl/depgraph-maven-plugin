package com.github.ferstl.depgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GraphFormatTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void forNameDot() {
    // arrange
    String name1 = "dot";
    String name2 = "Dot";
    String name3 = "DOT";

    // act
    GraphFormat result1 = GraphFormat.forName(name1);
    GraphFormat result2 = GraphFormat.forName(name2);
    GraphFormat result3 = GraphFormat.forName(name3);

    // assert
    assertSame(GraphFormat.DOT, result1);
    assertSame(GraphFormat.DOT, result2);
    assertSame(GraphFormat.DOT, result3);
  }

  @Test
  public void forNameGml() {
    // arrange
    String name1 = "gml";
    String name2 = "Gml";
    String name3 = "GML";

    // act
    GraphFormat result1 = GraphFormat.forName(name1);
    GraphFormat result2 = GraphFormat.forName(name2);
    GraphFormat result3 = GraphFormat.forName(name3);

    // assert
    assertSame(GraphFormat.GML, result1);
    assertSame(GraphFormat.GML, result2);
    assertSame(GraphFormat.GML, result3);
  }

  @Test
  public void forNameWithUnknownFormat() {
    // arrange
    this.expectedException.expect(IllegalArgumentException.class);
    this.expectedException.expectMessage("unknown_format");

    // act/assert
    GraphFormat.forName("unknown_format");
  }

  @Test
  public void getFileExtension() {
    assertEquals(".dot", GraphFormat.DOT.getFileExtension());
    assertEquals(".gml", GraphFormat.GML.getFileExtension());
  }
}
