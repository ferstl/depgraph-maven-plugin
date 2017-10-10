/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
  public void forNamePuml() {
    // arrange
    String name1 = "puml";
    String name2 = "Puml";
    String name3 = "PUML";

    // act
    GraphFormat result1 = GraphFormat.forName(name1);
    GraphFormat result2 = GraphFormat.forName(name2);
    GraphFormat result3 = GraphFormat.forName(name3);

    // assert
    assertSame(GraphFormat.PUML, result2);
    assertSame(GraphFormat.PUML, result1);
    assertSame(GraphFormat.PUML, result3);
  }


  @Test
  public void forNameJson() {
    // arrange
    String name1 = "json";
    String name2 = "Json";
    String name3 = "JSON";

    // act
    GraphFormat result1 = GraphFormat.forName(name1);
    GraphFormat result2 = GraphFormat.forName(name2);
    GraphFormat result3 = GraphFormat.forName(name3);

    // assert
    assertSame(GraphFormat.JSON, result2);
    assertSame(GraphFormat.JSON, result1);
    assertSame(GraphFormat.JSON, result3);
  }

  @Test
  public void forNameText() {
    // arrange
    String name1 = "text";
    String name2 = "Text";
    String name3 = "TEXT";

    // act
    GraphFormat result1 = GraphFormat.forName(name1);
    GraphFormat result2 = GraphFormat.forName(name2);
    GraphFormat result3 = GraphFormat.forName(name3);

    // assert
    assertSame(GraphFormat.TEXT, result2);
    assertSame(GraphFormat.TEXT, result1);
    assertSame(GraphFormat.TEXT, result3);
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
    assertEquals(".puml", GraphFormat.PUML.getFileExtension());
    assertEquals(".json", GraphFormat.JSON.getFileExtension());
    assertEquals(".txt", GraphFormat.TEXT.getFileExtension());
  }
}
