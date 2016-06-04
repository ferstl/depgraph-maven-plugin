package com.github.ferstl.depgraph.dot;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


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

}
