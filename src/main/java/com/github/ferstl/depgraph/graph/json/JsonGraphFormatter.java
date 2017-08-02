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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;
import com.google.common.base.Joiner;

public class JsonGraphFormatter implements GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    StringBuilder result = new StringBuilder();

    // output artifacts
    result.append("{ \"artifacts\":\n");
    result.append("  [ ");
    List<String> nodeStrings = new ArrayList<>();
    for (Node<?> node : nodes) {
      nodeStrings.add(node.getNodeName());
    }
    result.append(Joiner.on("\n  , ").join(nodeStrings));
    result.append("\n  ]\n");

    // output dependencies
    result.append(", \"dependencies\":\n");
    result.append("  [ ");
    List<String> depenencyStrings = new ArrayList<>();
    for (Edge edge : edges) {
      depenencyStrings.add(edge.getName());
    }
    result.append(Joiner.on("\n  , ").join(depenencyStrings));
    result.append("\n  ]\n");
    result.append("}");

    return result.toString();
  }
}
