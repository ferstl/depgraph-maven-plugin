package com.github.ferstl.depgraph.graph.text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TextGraphWriterTest {

  @Test
  public void rootWithOneChild() {
    // arrange
    TextGraphFormatter.TextGraphWriter writer = createTextGraphWriter(edge("A", "B"));

    // act
    StringBuilder result = new StringBuilder();
    writer.write(result);

    // assert
    String expected = "node-A\n"
        + "\\- node-B"
        + "\n";
    assertEquals(expected, result.toString());
  }

  @Test
  public void multipleRoots() {

  }

  @Test
  public void test() {
    StringBuilder stringBuilder = new StringBuilder();
    new TextGraphFormatter.TextGraphWriter(
        Arrays.<Node<?>>asList(
            node("A"),
            node("B"),
            node("C"),
            node("D"),
            node("E")
        ),
        Arrays.<Edge>asList(
            edge("A", "B", ""),
            edge("B", "C", ""),
            edge("A", "D", ""),
            edge("D", "E", "")
        )
    ).write(stringBuilder);

    System.out.println(stringBuilder);
  }

  private TextGraphFormatter.TextGraphWriter createTextGraphWriter(Edge... edges) {
    Set<Node<?>> nodes = new HashSet<>();
    for (Edge edge : edges) {
      nodes.add(node(edge.getFromNodeId()));
      nodes.add(node(edge.getToNodeId()));
    }

    return new TextGraphFormatter.TextGraphWriter(nodes, asList(edges));
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
