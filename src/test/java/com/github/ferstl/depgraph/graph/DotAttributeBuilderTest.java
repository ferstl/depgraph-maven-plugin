/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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
package com.github.ferstl.depgraph.graph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link DotAttributeBuilder}.
 */
public class DotAttributeBuilderTest {


  @Test
  public void empty() {
    assertEquals("", new DotAttributeBuilder().toString());
  }

  @Test
  public void label() {
    assertEquals("[label=\"someLabel\"]", new DotAttributeBuilder().label("someLabel").toString());
  }

  @Test
  public void htmlLabel() {
    String label = new DotAttributeBuilder().label("<<b>text1\ntext2</b><font point-size=\"10\">text3</font>>").toString();
    assertEquals("[label=<<b>text1\ntext2</b><font point-size=\"10\">text3</font>>]", label);
  }

  @Test
  public void fontName() {
    assertEquals("[fontname=\"Helvetica\"]", new DotAttributeBuilder().fontName("Helvetica").toString());
  }

  @Test
  public void fontSize() {
    assertEquals("[fontsize=\"12\"]", new DotAttributeBuilder().fontSize(12).toString());
  }

  @Test
  public void fontSizeZero() {
    assertEquals("", new DotAttributeBuilder().fontSize(0).toString());
  }

  @Test
  public void fontSizeNull() {
    assertEquals("", new DotAttributeBuilder().fontSize(null).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void fontSizeNegative() {
    new DotAttributeBuilder().fontSize(-1).toString();
  }

  @Test
  public void fontColor() {
    assertEquals("[fontcolor=\"green\"]", new DotAttributeBuilder().fontColor("green").toString());
  }

  @Test
  public void style() {
    assertEquals("[style=\"dashed\"]", new DotAttributeBuilder().style("dashed").toString());
  }

  @Test
  public void color() {
    assertEquals("[color=\"blue\"]", new DotAttributeBuilder().color("blue").toString());
  }

  @Test
  public void fillColor() {
    assertEquals("[fillcolor=\"red\"]", new DotAttributeBuilder().fillColor("red").toString());
  }

  @Test
  public void shape() {
    assertEquals("[shape=\"box\"]", new DotAttributeBuilder().shape("box").toString());
  }

  @Test
  public void addAttribute() {
    assertEquals("[someAttribute=\"someValue\"]", new DotAttributeBuilder().addAttribute("someAttribute", "someValue").toString());
  }

  @Test
  public void addAttributeWithNullValue() {
    assertEquals("", new DotAttributeBuilder().addAttribute("someAttribute", null).toString());
  }

  @Test
  public void multipleAttributes() {
    assertEquals("[label=\"someLabel\",color=\"green\",fontsize=\"10\"]", new DotAttributeBuilder().label("someLabel").color("green").fontSize(10).toString());
  }

  @Test
  public void quoting() {
    assertEquals("[label=\"some Label\"]", new DotAttributeBuilder().label("some Label").toString());
  }

  @Test
  public void escaping() {
    assertEquals("[label=\"some\\nLabel\"]", new DotAttributeBuilder().label("some\nLabel").toString());
  }

}
