package com.github.ferstl.depgraph.graph.text;

import java.util.Arrays;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

public class TextGraphWriterTest {

  @Test
  public void test() {
    StringBuilder stringBuilder = new StringBuilder();
    new TextGraphWriter(
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

  private Node<?> node(String id) {
    return new Node<>(id, "name-" + id, new Object());
  }

  private Edge edge(String from, String to, String name) {
    return new Edge(from, to, name);
  }
}
