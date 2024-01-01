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
package com.github.ferstl.depgraph.graph.mermaid;


import com.github.ferstl.depgraph.dependency.mermaid.MermaidDependencyEdgeRenderer;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class MermaidGraphFormatter implements GraphFormatter {

  private Collection<MermaidEdgeStyle> styles = new LinkedHashSet<>();

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder sb = new StringBuilder("flowchart TD");

    sb.append("\n  %% Node Definitions:");
    for (Node<?> node : nodes) {
      String nodeId = node.getNodeId();
      String nodeName = node.getNodeName();
      sb.append("\n  ")
          .append(nodeId);
      if( nodeName != null && !nodeName.isEmpty()) {
          sb.append(nodeName);
      }
    }

    sb.append("\n\n  %% Edge Definitions:");
    int row = 0;
    for (Edge edge : edges) {
      String edgeName = edge.getName();
      /*
        Edge style can't be defined on the same line. It has to be added after (See https://mermaid-js.github.io/mermaid/#/flowchart?id=styling-links)
        So the right style, on the right index is added to the styles list.
       */
      if(MermaidDependencyEdgeRenderer.DUPLICATE_PREFIX.equals(edgeName)) {
        styles.add(MermaidEdgeStyle.createDuplicateStyle(row));
        edgeName = "";
      } else if (edgeName.startsWith(MermaidDependencyEdgeRenderer.CONFLICT_PREFIX)) {
        styles.add(MermaidEdgeStyle.createConflictStyle(row));
        edgeName = edgeName.substring(1);
      }

      if (!edgeName.isEmpty()) {
        edgeName = "-" + edgeName + "-";
      }

      String edgeDefinition = edge.getFromNodeId() + " -" + edgeName + "-> " + edge.getToNodeId();
      sb.append("\n  ").append(edgeDefinition);

      row++;
    }

    /*
      Add styles to the end of the graph definition.
     */
    if (!styles.isEmpty()) {
      sb.append("\n\n  %% Edge Styles:");
      for(MermaidEdgeStyle style : styles) {
        sb.append("\n  ").append(style.toString());
      }
    }

    return sb.toString();
  }
}
