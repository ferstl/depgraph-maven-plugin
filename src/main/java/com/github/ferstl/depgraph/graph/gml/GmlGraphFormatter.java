/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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
package com.github.ferstl.depgraph.graph.gml;

import java.util.Collection;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class GmlGraphFormatter implements GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder result = new StringBuilder();
    result.append("graph [\n");

    //output nodes
    for (Node<?> node : nodes) {
      result.append("node [\n");
      result.append("id \"").append(node.getNodeId()).append("\"\n");
      if (isNotBlank(node.getNodeName())) {
        result.append(node.getNodeName()).append("\n");
      }
      result.append("]\n\n");
    }

    //output edges
    for (Edge edge : edges) {
      result.append("edge [\n");
      result.append("source \"").append(edge.getFromNodeId()).append("\"\n");
      result.append("target \"").append(edge.getToNodeId()).append("\"\n");
      if (isNotBlank(edge.getName())) {
        result.append(edge.getName()).append("\n");
      }
      result.append("]\n\n");
    }

    result.append("]");
    return result.toString();
  }
}
