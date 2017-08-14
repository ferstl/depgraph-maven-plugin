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
package com.github.ferstl.depgraph.graph.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

public class JsonGraphFormatter implements GraphFormatter {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .setSerializationInclusion(NON_EMPTY)
      .setVisibility(FIELD, ANY);

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    Map<String, Integer> nodeIdMap = new HashMap<>(nodes.size());
    JsonGraph jsonGraph = new JsonGraph(graphName);

    int numericNodeId = 0;
    for (Node<?> node : nodes) {
      String nodeId = node.getNodeId();
      nodeIdMap.put(nodeId, numericNodeId++);
      jsonGraph.addArtifact(nodeId, numericNodeId, readJson(node.getNodeName()));
    }

    for (Edge edge : edges) {
      String fromNodeId = edge.getFromNodeId();
      Integer fromNodeIdNumeric = nodeIdMap.get(fromNodeId);
      String toNodeId = edge.getToNodeId();
      Integer toNodeIdNumeric = nodeIdMap.get(toNodeId);
      jsonGraph.addDependency(fromNodeId, fromNodeIdNumeric, toNodeId, toNodeIdNumeric, readJson(edge.getName()));
    }

    return serialize(jsonGraph);
  }

  private Map<?, ?> readJson(String json) {
    try {
      return this.objectMapper.readValue(json, Map.class);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read JSON '" + json + "'", e);
    }
  }

  private String serialize(JsonGraph jsonGraph) {
    ObjectWriter writer = this.objectMapper.writerWithDefaultPrettyPrinter();
    StringWriter jsonWriter = new StringWriter();
    try {
      writer.writeValue(jsonWriter, jsonGraph);
    } catch (IOException e) {
      // should never happen with StringWriter
      throw new IllegalStateException(e);
    }

    return jsonWriter.toString();
  }

}
