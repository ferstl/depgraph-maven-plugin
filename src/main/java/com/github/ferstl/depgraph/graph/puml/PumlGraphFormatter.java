/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
package com.github.ferstl.depgraph.graph.puml;

import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;
import com.github.ferstl.depgraph.puml.PumlEdgeInfo;
import com.github.ferstl.depgraph.puml.PumlNodeInfo;

/**
 * Graph formatter for <a href="PlantUML">http://plantuml.com/component-diagram</a> diagram.
 */
public class PumlGraphFormatter implements GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    final StringBuilder puml = new StringBuilder();

    startUml(puml);
    skinParam(puml);
    writeNodes(puml, nodes);
    writeEdges(puml, edges);
    endUml(puml);

    return puml.toString();
  }

  private void startUml(StringBuilder puml) {
    puml.append("@startuml\n");
  }

  private void skinParam(StringBuilder puml) {
    puml.append("skinparam rectangle {\n")
        .append("  BackgroundColor<<test>> lightGreen\n")
        .append("  BackgroundColor<<runtime>> lightBlue\n")
        .append("  BackgroundColor<<provided>> lightGray\n")
        .append("}\n");
  }

  private void writeNodes(StringBuilder puml, Collection<Node<?>> nodes) {
    for (Node<?> node : nodes) {

      final PumlNodeInfo nodeInfo = PumlNodeInfo.parse(node.getNodeName());

      puml.append(nodeInfo.getComponent())
          .append(" \"")
          .append(nodeInfo.getLabel())
          .append("\" as ")
          .append(escape(node.getNodeId()));


      if (!nodeInfo.getStereotype().equals("compile")) {
        puml.append("<<")
            .append(nodeInfo.getStereotype())
            .append(">>");
      }


      puml.append("\n");
    }
  }

  private void writeEdges(StringBuilder puml, Collection<Edge> edges) {
    for (Edge edge : edges) {
      final PumlEdgeInfo edgeInfo = PumlEdgeInfo.parse(edge.getName());
      puml.append(escape(edge.getFromNodeId()))
          .append(" ")
          .append(edgeInfo.getBegin())
          .append(edgeInfo.getColor())
          .append(edgeInfo.getEnd())
          .append(" ")
          .append(escape(edge.getToNodeId()));

      if (edgeInfo.getLabel() != null && !edgeInfo.getLabel().equals("")) {
        puml.append(": ")
            .append(edgeInfo.getLabel());
      }

      puml.append("\n");
    }
  }

  private void endUml(StringBuilder puml) {
    puml.append("@enduml");
  }

  private String escape(String id) {
    return StringUtils.removeEnd(id.replaceAll("\\W", "_"), "_");
  }
}
