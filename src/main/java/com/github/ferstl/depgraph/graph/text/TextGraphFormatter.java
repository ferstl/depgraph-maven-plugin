package com.github.ferstl.depgraph.graph.text;

import java.util.Collection;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

public class TextGraphFormatter implements com.github.ferstl.depgraph.graph.GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    TextGraphWriter writer = new TextGraphWriter(nodes, edges);
    StringBuilder graphStringBuilder = new StringBuilder();
    writer.write(graphStringBuilder);

    return graphStringBuilder.toString();
  }
}
