package com.github.ferstl.depgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class OutputFormatTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void forNameDot() {
    // arrange
    String name1 = "dot";
    String name2 = "Dot";
    String name3 = "DOT";

    // act
    OutputFormat result1 = OutputFormat.forName(name1);
    OutputFormat result2 = OutputFormat.forName(name2);
    OutputFormat result3 = OutputFormat.forName(name3);

    // assert
    assertSame(OutputFormat.DOT, result1);
    assertSame(OutputFormat.DOT, result2);
    assertSame(OutputFormat.DOT, result3);
  }

  @Test
  public void forNameGml() {
    // arrange
    String name1 = "gml";
    String name2 = "Gml";
    String name3 = "GML";

    // act
    OutputFormat result1 = OutputFormat.forName(name1);
    OutputFormat result2 = OutputFormat.forName(name2);
    OutputFormat result3 = OutputFormat.forName(name3);

    // assert
    assertSame(OutputFormat.GML, result1);
    assertSame(OutputFormat.GML, result2);
    assertSame(OutputFormat.GML, result3);
  }

  @Test
  public void forNameWithUnknownFormat() {
    // arrange
    this.expectedException.expect(IllegalArgumentException.class);
    this.expectedException.expectMessage("unknown_format");

    // act/assert
    OutputFormat.forName("unknown_format");
  }

  @Test
  public void getFileExtension() {
    assertEquals(".dot", OutputFormat.DOT.getFileExtension());
    assertEquals(".gml", OutputFormat.GML.getFileExtension());
  }
}
