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
package com.github.ferstl.depgraph.graph.mermaid;

import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MermaidGraphFormatterTest {

  private MermaidGraphFormatter formatter = new MermaidGraphFormatter();

  @Test
  void format() {
    // arrange
    Node<?> node1 = new Node<>("id1", "[\"name1\"]", new Object());
    Node<?> node2 = new Node<>("id2", "", new Object());
    Node<?> node3 = new Node<>("id3", "[\"name3\"]", new Object());

    Edge edge1 = new Edge("id1", "id2", "[\"edge1\"]");
    Edge edge2 = new Edge("id1", "id2", "");

    // act
    String result = this.formatter.format("graphName", asList(node1, node2, node3), asList(edge1, edge2));

    // assert
    String expected = "flowchart TD\n"
        + "  %% Node Definitions:\n"
        + "  id1[\"name1\"]\n"
        + "  id2\n"
        + "  id3[\"name3\"]\n"
        + "\n"
        + "  %% Edge Definitions:\n"
        + "  id1 --[\"edge1\"]--> id2\n"
        + "  id1 --> id2"
        ;

    assertEquals(expected, result);
  }
}
