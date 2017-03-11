package com.github.ferstl.depgraph.graph.dot;

import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class DotGraphFormatterTest {

  private DotGraphFormatter formatter;

  @Before
  public void before() {
    DotAttributeBuilder graphAttributeBuilder = new DotAttributeBuilder();
    DotAttributeBuilder nodeAttributeBuilder = new DotAttributeBuilder().shape("box").fontName("Helvetica");
    DotAttributeBuilder edgeAttributeBuilder = new DotAttributeBuilder().fontName("Helvetica").fontSize(10);
    this.formatter = new DotGraphFormatter(graphAttributeBuilder, nodeAttributeBuilder, edgeAttributeBuilder);
  }

  @Test
  public void format() {
    // arrange
    Node<?> node1 = new Node<>("id1", "name1", new Object());
    Node<?> node2 = new Node<>("id2", "", new Object());
    Node<?> node3 = new Node<>("id3", "name3", new Object());

    Edge edge1 = new Edge("id1", "id2", "edge1");
    Edge edge2 = new Edge("id1", "id2", "");

    // act
    String result = this.formatter.format("graphName", asList(node1, node2, node3), asList(edge1, edge2));

    // assert
    String expected = "digraph \"graphName\" {\n"
        + "  node [shape=\"box\",fontname=\"Helvetica\"]\n"
        + "  edge [fontname=\"Helvetica\",fontsize=\"10\"]\n"
        + "\n"
        + "  // Node Definitions:\n"
        + "  \"id1\"name1\n"
        + "  \"id2\"\n"
        + "  \"id3\"name3\n"
        + "\n"
        + "  // Edge Definitions:\n"
        + "  \"id1\" -> \"id2\"edge1\n"
        + "  \"id1\" -> \"id2\"\n"
        + "}";

    assertEquals(expected, result);
  }
}
