/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
package com.github.ferstl.depgraph.graph.dot;


import java.util.Collection;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;

import static com.github.ferstl.depgraph.graph.dot.DotEscaper.escape;

public class DotGraphFormatter implements GraphFormatter {

  private final DotAttributeBuilder graphAttributeBuilder;
  private final DotAttributeBuilder nodeAttributeBuilder;
  private final DotAttributeBuilder edgeAttributeBuilder;

  public DotGraphFormatter() {
    this.graphAttributeBuilder = new DotAttributeBuilder();
    this.nodeAttributeBuilder = new DotAttributeBuilder().shape("box").fontName("Helvetica");
    this.edgeAttributeBuilder = new DotAttributeBuilder().fontName("Helvetica").fontSize(10);
  }

  public DotGraphFormatter(DotAttributeBuilder graphAttributeBuilder, DotAttributeBuilder nodeAttributeBuilder, DotAttributeBuilder edgeAttributeBuilder) {
    this.graphAttributeBuilder = graphAttributeBuilder;
    this.nodeAttributeBuilder = nodeAttributeBuilder;
    this.edgeAttributeBuilder = edgeAttributeBuilder;
  }

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder sb = new StringBuilder("digraph ").append(escape(graphName)).append(" {");
    appendAttributes("graph", this.graphAttributeBuilder, sb);
    appendAttributes("node", this.nodeAttributeBuilder, sb);
    appendAttributes("edge", this.edgeAttributeBuilder, sb);

    sb.append("\n\n  // Node Definitions:");
    for (Node<?> node : nodes) {
      String nodeId = node.getNodeId();
      String nodeName = node.getNodeName();
      sb.append("\n  ")
          .append(escape(nodeId))
          .append(nodeName);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (Edge edge : edges) {
      String edgeDefinition = escape(edge.getFromNodeId()) + " -> " + escape(edge.getToNodeId()) + edge.getName();
      sb.append("\n  ").append(edgeDefinition);
    }

    return sb.append("\n}").toString();
  }

  private void appendAttributes(String tagName, DotAttributeBuilder attributeBuilder, StringBuilder sb) {
    if (!attributeBuilder.isEmpty()) {
      sb.append("\n  ")
          .append(tagName)
          .append(" ")
          .append(attributeBuilder);
    }
  }
}
