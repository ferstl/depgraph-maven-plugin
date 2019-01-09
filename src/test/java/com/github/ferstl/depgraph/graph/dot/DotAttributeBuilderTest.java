/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link DotAttributeBuilder}.
 */
class DotAttributeBuilderTest {


  @Test
  void empty() {
    assertEquals("", new DotAttributeBuilder().toString());
  }

  @Test
  void label() {
    assertEquals("[label=\"someLabel\"]", new DotAttributeBuilder().label("someLabel").toString());
  }

  @Test
  void htmlLabel() {
    String label = new DotAttributeBuilder().label("<<b>text1\ntext2</b><font point-size=\"10\">text3</font>>").toString();
    assertEquals("[label=<<b>text1\ntext2</b><font point-size=\"10\">text3</font>>]", label);
  }

  @Test
  void fontName() {
    assertEquals("[fontname=\"Helvetica\"]", new DotAttributeBuilder().fontName("Helvetica").toString());
  }

  @Test
  void fontSize() {
    assertEquals("[fontsize=\"12\"]", new DotAttributeBuilder().fontSize(12).toString());
  }

  @Test
  void fontSizeZero() {
    assertEquals("", new DotAttributeBuilder().fontSize(0).toString());
  }

  @Test
  void fontSizeNull() {
    assertEquals("", new DotAttributeBuilder().fontSize(null).toString());
  }

  @Test
  void fontSizeNegative() {
    assertThrows(IllegalArgumentException.class, () -> new DotAttributeBuilder().fontSize(-1).toString());
  }

  @Test
  void fontColor() {
    assertEquals("[fontcolor=\"green\"]", new DotAttributeBuilder().fontColor("green").toString());
  }

  @Test
  void style() {
    assertEquals("[style=\"dashed\"]", new DotAttributeBuilder().style("dashed").toString());
  }

  @Test
  void color() {
    assertEquals("[color=\"blue\"]", new DotAttributeBuilder().color("blue").toString());
  }

  @Test
  void fillColor() {
    assertEquals("[fillcolor=\"red\"]", new DotAttributeBuilder().fillColor("red").toString());
  }

  @Test
  void shape() {
    assertEquals("[shape=\"box\"]", new DotAttributeBuilder().shape("box").toString());
  }

  @Test
  void rankdir() {
    assertEquals("[rankdir=\"LR\"]", new DotAttributeBuilder().rankdir("LR").toString());
  }

  @Test
  void addAttribute() {
    assertEquals("[someAttribute=\"someValue\"]", new DotAttributeBuilder().addAttribute("someAttribute", "someValue").toString());
  }

  @Test
  void addAttributeWithNullValue() {
    assertEquals("", new DotAttributeBuilder().addAttribute("someAttribute", null).toString());
  }

  @Test
  void multipleAttributes() {
    assertEquals("[label=\"someLabel\",color=\"green\",fontsize=\"10\"]", new DotAttributeBuilder().label("someLabel").color("green").fontSize(10).toString());
  }

  @Test
  void quoting() {
    assertEquals("[label=\"some Label\"]", new DotAttributeBuilder().label("some Label").toString());
  }

  @Test
  void escaping() {
    assertEquals("[label=\"some\\nLabel\"]", new DotAttributeBuilder().label("some\nLabel").toString());
  }

  @Test
  void isEmptyWithData() {
    assertFalse(new DotAttributeBuilder().label("some label").isEmpty());
  }

  @Test
  void isEmptyWithoutData() {
    assertTrue(new DotAttributeBuilder().isEmpty());
  }

}
