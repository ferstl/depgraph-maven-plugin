package com.github.ferstl.depgraph.graph;

import java.util.Map;
import com.google.common.collect.ImmutableMap;

public class StyleConfiguration {

  NodeConfiguration defaultNode = new NodeConfiguration();
  EdgeConfiguration defaultEdge = new EdgeConfiguration();
  Map<String, NodeConfiguration> scopedNodes = ImmutableMap.of("compile", new NodeConfiguration(), "test", new NodeConfiguration());
  Map<NodeResolution, EdgeConfiguration> edgeTypes = ImmutableMap.of(NodeResolution.INCLUDED, new EdgeConfiguration(), NodeResolution.OMITTED_FOR_DUPLICATE, new EdgeConfiguration());


  static class NodeConfiguration {

    String shape = "polygon";
    int sides = 4;
    String color = "red";
    Font font = new Font();
  }

  static class EdgeConfiguration {

    String style = "dotted";
    String color = "black";
    Font font = new Font();
  }

  static class Font {

    String color = "black";
    int size = 14;
    String name = "Helvetica";
  }
}
