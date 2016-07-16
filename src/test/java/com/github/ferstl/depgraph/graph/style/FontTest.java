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
