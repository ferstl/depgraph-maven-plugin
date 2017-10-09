package com.github.ferstl.depgraph.graph.text;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TextGraphFormatterTest {

  @Test
  @Ignore
  public void indentation() {
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
  public void rootWithOneChild() {
    // arrange + act
    String result = createTextGraph(edge("parent", "child"));

    // assert
    String expected = "parent\n"
        + "\\- child\n";
    assertEquals(expected, result);
  }

  @Test
  public void multipleRoots() {
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

  private String createTextGraph(Edge... edges) {
    Set<Node<?>> nodes = new LinkedHashSet<>();
    for (Edge edge : edges) {
      nodes.add(node(edge.getFromNodeId()));
      nodes.add(node(edge.getToNodeId()));
    }

    return new TextGraphFormatter().format("", nodes, asList(edges));
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
