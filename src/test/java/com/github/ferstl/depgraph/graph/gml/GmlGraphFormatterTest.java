package com.github.ferstl.depgraph.graph.gml;

import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class GmlGraphFormatterTest {

  private GmlGraphFormatter formatter;

  @Before
  public void before() {
    this.formatter = new GmlGraphFormatter();
  }

  @Test
  public void format() {
    // arrange
    Node<?> node1 = new Node<>("id1", "label \"name1\"", new Object());
    Node<?> node2 = new Node<>("id2", "", new Object());
    Node<?> node3 = new Node<>("id3", "label \"name3\"", new Object());

    Edge edge1 = new Edge("id1", "id2", "label \"edge1\"");
    Edge edge2 = new Edge("id1", "id2", "");

    // act
    String result = this.formatter.format("graphName", asList(node1, node2, node3), asList(edge1, edge2));

    // assert
    String expected = "graph [\n"
        + "node [\n"
        + "id \"id1\"\n"
        + "label \"name1\"\n"
        + "]\n"
        + "\n"
        + "node [\n"
        + "id \"id2\"\n"
        + "]\n"
        + "\n"
        + "node [\n"
        + "id \"id3\"\n"
        + "label \"name3\"\n"
        + "]\n"
        + "\n"
        + "edge [\n"
        + "source \"id1\"\n"
        + "target \"id2\"\n"
        + "label \"edge1\"\n"
        + "]\n"
        + "\n"
        + "edge [\n"
        + "source \"id1\"\n"
        + "target \"id2\"\n"
        + "]\n"
        + "\n"
        + "]";

    assertEquals(expected, result);
  }
}
