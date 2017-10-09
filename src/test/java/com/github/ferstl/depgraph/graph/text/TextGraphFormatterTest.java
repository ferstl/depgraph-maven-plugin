package com.github.ferstl.depgraph.graph.text;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TextGraphFormatterTest {

  @Test
  public void rootWithOneChild() {
    // arrange + act
    String result = createTextGraph(edge("A", "B"));

    // assert
    String expected = "node-A\n"
        + "\\- node-B"
        + "\n";
    assertEquals(expected, result);
  }

  @Test
  public void multipleRoots() {

  }

  private String createTextGraph(Edge... edges) {
    Set<Node<?>> nodes = new HashSet<>();
    for (Edge edge : edges) {
      nodes.add(node(edge.getFromNodeId()));
      nodes.add(node(edge.getToNodeId()));
    }

    return new TextGraphFormatter().format("", nodes, asList(edges));
  }

  private Node<?> node(String id) {
    return new Node<>(id, "node-" + id, new Object());
  }

  private Edge edge(String from, String to) {
    return new Edge(from, to, "");
  }

  private Edge edge(String from, String to, String name) {
    return new Edge(from, to, name);
  }
}
