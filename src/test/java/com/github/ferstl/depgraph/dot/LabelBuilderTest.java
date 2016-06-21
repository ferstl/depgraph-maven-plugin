package com.github.ferstl.depgraph.dot;

import org.junit.Test;
import static org.hamcrest.Matchers.emptyString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class LabelBuilderTest {

  @Test
  public void mixed() {
    String label = new LabelBuilder()
        .bold("bold text")
        .text("intermediate text")
        .italic("italic text")
        .newLine()
        .underline("underlined text")
        .text("intermediate text")
        .font()
        .color("red")
        .size(20)
        .name("Sans")
        .text("special")
        .text("intermediate text")
        .build();

    assertEquals(
        "<<b>bold text</b>"
            + "intermediate text"
            + "<i>italic text</i>"
            + "<br/>"
            + "<u>underlined text</u>"
            + "intermediate text"
            + "<font color=\"red\" face=\"Sans\" point-size=\"20\">special</font>"
            + "intermediate text>",
        label);
  }

  @Test
  public void htmlEscape() {
    String label = new LabelBuilder()
        .text("<foo>")
        .underline("&bar&")
        .build();

    assertEquals("<&lt;foo&gt;<u>&amp;bar&amp;</u>>", label);
  }

  @Test
  public void text() {
    String label = new LabelBuilder()
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  public void bold() {
    String label = new LabelBuilder()
        .bold("text")
        .build();

    assertEquals("<<b>text</b>>", label);
  }

  @Test
  public void italic() {
    String label = new LabelBuilder()
        .italic("text")
        .build();

    assertEquals("<<i>text</i>>", label);
  }

  @Test
  public void underline() {
    String label = new LabelBuilder()
        .underline("text")
        .build();

    assertEquals("<<u>text</u>>", label);
  }

  @Test
  public void newLine() {
    String label = new LabelBuilder()
        .text("text")
        .newLine()
        .build();

    assertEquals("<text<br/>>", label);
  }

  @Test
  public void smartNewLineAtBeginning() {
    String label = new LabelBuilder()
        .smartNewLine()
        .text("text")
        .smartNewLine()
        .build();

    assertEquals("<text<br/>>", label);
  }

  @Test
  public void smartNewLineWithinText() {
    String label = new LabelBuilder()
        .text("text1")
        .smartNewLine()
        .smartNewLine()
        .text("text2")
        .smartNewLine()
        .build();

    assertEquals("<text1<br/>text2<br/>>", label);
  }

  @Test
  public void fontName() {
    String label = new LabelBuilder()
        .font()
        .name("Helvetica")
        .text("text")
        .build();

    assertEquals("<<font face=\"Helvetica\">text</font>>", label);
  }

  @Test
  public void fontSize() {
    String label = new LabelBuilder()
        .font()
        .size(10)
        .text("text")
        .build();

    assertEquals("<<font point-size=\"10\">text</font>>", label);
  }

  @Test
  public void fontSizeZero() {
    String label = new LabelBuilder()
        .font()
        .size(0)
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  public void fontSizeNull() {
    String label = new LabelBuilder()
        .font()
        .size(null)
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fontSizeNegative() {
    new LabelBuilder()
        .font()
        .size(-1);
  }

  @Test
  public void fontColor() {
    String label = new LabelBuilder()
        .font()
        .color("red")
        .text("text")
        .build();

    assertEquals("<<font color=\"red\">text</font>>", label);
  }

  @Test
  public void fontAll() {
    String label = new LabelBuilder()
        .font()
        .name("Helvetica")
        .color("green")
        .size(12)
        .text("text")
        .build();

    assertEquals("<<font color=\"green\" face=\"Helvetica\" point-size=\"12\">text</font>>", label);
  }

  @Test
  public void fontNullValues() {
    String label = new LabelBuilder()
        .font()
        .name(null)
        .color(null)
        .size(12)
        .text("text")
        .build();

    assertEquals("<<font point-size=\"12\">text</font>>", label);
  }

  @Test
  public void fontEmpty() {
    String label = new LabelBuilder()
        .font()
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  public void empty() {
    String label = new LabelBuilder().build();

    assertThat(label, emptyString());
  }

  @Test
  public void emptyText() {
    String label = new LabelBuilder()
        .text(null)
        .font()
        .color("red")
        .text("")
        .italic(null)
        .bold("")
        .underline(null)
        .build();

    assertThat(label, emptyString());
  }
}
