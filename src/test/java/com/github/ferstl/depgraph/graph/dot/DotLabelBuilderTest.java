/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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
package com.github.ferstl.depgraph.graph.dot;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DotLabelBuilderTest {

  @Test
  void mixed() {
    String label = new DotLabelBuilder()
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
  void htmlEscape() {
    String label = new DotLabelBuilder()
        .text("<foo>")
        .underline("&bar&")
        .build();

    assertEquals("<&lt;foo&gt;<u>&amp;bar&amp;</u>>", label);
  }

  @Test
  void text() {
    String label = new DotLabelBuilder()
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void bold() {
    String label = new DotLabelBuilder()
        .bold("text")
        .build();

    assertEquals("<<b>text</b>>", label);
  }

  @Test
  void italic() {
    String label = new DotLabelBuilder()
        .italic("text")
        .build();

    assertEquals("<<i>text</i>>", label);
  }

  @Test
  void underline() {
    String label = new DotLabelBuilder()
        .underline("text")
        .build();

    assertEquals("<<u>text</u>>", label);
  }

  @Test
  void newLine() {
    String label = new DotLabelBuilder()
        .text("text")
        .newLine()
        .build();

    assertEquals("<text<br/>>", label);
  }

  @Test
  void smartNewLineAtBeginning() {
    String label = new DotLabelBuilder()
        .smartNewLine()
        .text("text")
        .smartNewLine()
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void smartNewLineWithEmptyTextAtBeginning() {
    String label = new DotLabelBuilder()
        .font()
        .size(10)
        .color("green")
        .text(null)
        .smartNewLine()
        .text("text")
        .smartNewLine()
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void smartNewLineWithinText() {
    String label = new DotLabelBuilder()
        .text("text1")
        .smartNewLine()
        .smartNewLine()
        .text("text2")
        .smartNewLine()
        .build();

    assertEquals("<text1<br/>text2>", label);
  }

  @Test
  void fontName() {
    String label = new DotLabelBuilder()
        .font()
        .name("Helvetica")
        .text("text")
        .build();

    assertEquals("<<font face=\"Helvetica\">text</font>>", label);
  }

  @Test
  void fontSize() {
    String label = new DotLabelBuilder()
        .font()
        .size(10)
        .text("text")
        .build();

    assertEquals("<<font point-size=\"10\">text</font>>", label);
  }

  @Test
  void fontSizeZero() {
    String label = new DotLabelBuilder()
        .font()
        .size(0)
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void fontSizeNull() {
    String label = new DotLabelBuilder()
        .font()
        .size(null)
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void fontSizeNegative() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DotLabelBuilder()
            .font()
            .size(-1));
  }

  @Test
  void fontColor() {
    String label = new DotLabelBuilder()
        .font()
        .color("red")
        .text("text")
        .build();

    assertEquals("<<font color=\"red\">text</font>>", label);
  }

  @Test
  void fontAll() {
    String label = new DotLabelBuilder()
        .font()
        .name("Helvetica")
        .color("green")
        .size(12)
        .text("text")
        .build();

    assertEquals("<<font color=\"green\" face=\"Helvetica\" point-size=\"12\">text</font>>", label);
  }

  @Test
  void fontNullValues() {
    String label = new DotLabelBuilder()
        .font()
        .name(null)
        .color(null)
        .size(12)
        .text("text")
        .build();

    assertEquals("<<font point-size=\"12\">text</font>>", label);
  }

  @Test
  void fontEmpty() {
    String label = new DotLabelBuilder()
        .font()
        .text("text")
        .build();

    assertEquals("<text>", label);
  }

  @Test
  void empty() {
    String label = new DotLabelBuilder().build();

    assertThat(label, emptyString());
  }

  @Test
  void emptyText() {
    String label = new DotLabelBuilder()
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
