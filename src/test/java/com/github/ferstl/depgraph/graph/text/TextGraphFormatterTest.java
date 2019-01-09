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
package com.github.ferstl.depgraph.graph.text;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextGraphFormatterTest {

  @Test
  void indentation() {
    // arrange + act
    String result = createTextGraph(
        edge("root", "child-1"),
        edge("root", "child-2"),
        edge("root", "child-3"),

        edge("child-1", "child-1.1"),
        edge("child-1.1", "child-1.1.1"),
        edge("child-1.1.1", "child-1.1.1.1"),
        edge("child-1.1.1.1", "child-1.1.1.1.1"),
        edge("child-1.1.1", "child-1.1.1.2"),
        edge("child-1.1", "child-1.1.2"),

        edge("child-3", "child-3.1"),
        edge("child-3", "child-3.2"),
        edge("child-3", "child-3.3"),
        edge("child-3.3", "child-3.3.1"));

    // assert
    String expected = "root\n"
        + "+- child-1\n"
        + "|  \\- child-1.1\n"
        + "|     +- child-1.1.1\n"
        + "|     |  +- child-1.1.1.1\n"
        + "|     |  |  \\- child-1.1.1.1.1\n"
        + "|     |  \\- child-1.1.1.2\n"
        + "|     \\- child-1.1.2\n"
        + "+- child-2\n"
        + "\\- child-3\n"
        + "   +- child-3.1\n"
        + "   +- child-3.2\n"
        + "   \\- child-3.3\n"
        + "      \\- child-3.3.1\n";
    assertEquals(expected, result);
  }


  @Test
  void rootWithOneChild() {
    // arrange + act
    String result = createTextGraph(edge("parent", "child"));

    // assert
    String expected = "parent\n"
        + "\\- child\n";
    assertEquals(expected, result);
  }

  @Test
  void namedEdge() {
    // arrange + act
    String result = createTextGraph(
        edge("root", "child-1", "description"),
        edge("root", "child-2"));

    // assert
    String expected = "root\n"
        + "+- child-1 (description)\n"
        + "\\- child-2\n";
    assertEquals(expected, result);
  }

  @Test
  void multipleRoots() {
    // arrange + act
    String result = createTextGraph(
        edge("root-1", "child-1.1"),
        edge("root-1", "child-1.2"),
        edge("child-1.2", "child-1.2.1"),

        edge("root-2", "child-2.1"),
        edge("root-2", "child-2.2"),

        edge("root-3", "child-3.1"));

    // assert
    String expected = "root-1\n"
        + "+- child-1.1\n"
        + "\\- child-1.2\n"
        + "   \\- child-1.2.1\n"
        + "root-2\n"
        + "+- child-2.1\n"
        + "\\- child-2.2\n"
        + "root-3\n"
        + "\\- child-3.1\n";
    assertEquals(expected, result);
  }

  @Test
  void unrepeatedTransitiveDependencies() {
    // arrange + act
    String result = createTextGraph(
        edge("root", "child-1"),
        edge("root", "child-2"),
        edge("child-1", "child-1.1"),
        edge("child-2", "child-1"));

    // assert
    String expected = "root\n"
        + "+- child-1\n"
        + "|  \\- child-1.1\n"
        + "\\- child-2\n"
        + "   \\- child-1\n";
    assertEquals(expected, result);
  }

  @Test
  void repeatedTransitiveDependencies() {
    // arrange + act
    String result = createTextGraph(
        true,
        edge("root", "child-1"),
        edge("root", "child-2"),
        edge("child-1", "child-1.1"),
        edge("child-2", "child-1"));

    // assert
    String expected = "root\n"
        + "+- child-1\n"
        + "|  \\- child-1.1\n"
        + "\\- child-2\n"
        + "   \\- child-1\n"
        + "      \\- child-1.1\n";
    assertEquals(expected, result);
  }

  private String createTextGraph(Edge... edges) {
    return createTextGraph(false, edges);
  }

  private String createTextGraph(boolean repeatTransitiveDependencies, Edge... edges) {
    Set<Node<?>> nodes = new LinkedHashSet<>();
    for (Edge edge : edges) {
      nodes.add(node(edge.getFromNodeId()));
      nodes.add(node(edge.getToNodeId()));
    }

    return new TextGraphFormatter(repeatTransitiveDependencies).format("", nodes, asList(edges));
  }

  private Node<?> node(String id) {
    return new Node<>(id, id, new Object());
  }

  private Edge edge(String from, String to) {
    return new Edge(from, to, "");
  }

  private Edge edge(String from, String to, String name) {
    return new Edge(from, to, name);
  }
}
