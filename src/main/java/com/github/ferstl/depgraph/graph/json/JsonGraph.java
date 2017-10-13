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
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

class JsonGraph {

  private final String graphName;
  private final List<Artifact> artifacts = new ArrayList<>();
  private final List<Dependency> dependencies = new ArrayList<>();

  JsonGraph(String graphName) {
    this.graphName = graphName;
  }

  void addArtifact(String nodeId, int numericNodeId, Map<?, ?> data) {
    this.artifacts.add(new Artifact(nodeId, numericNodeId, data));
  }

  void addDependency(String fromNodeId, int fromNodeIdNumeric, String toNodeId, int toNodeIdNumeric, Map<?, ?> data) {
    this.dependencies.add(new Dependency(fromNodeId, fromNodeIdNumeric, toNodeId, toNodeIdNumeric, data));
  }

  private static class Artifact {

    private final String id;
    private final int numericId;
    @JsonIgnore
    private final Map<?, ?> data;

    Artifact(String id, int numericId, Map<?, ?> data) {
      this.id = id;
      this.numericId = numericId;
      this.data = data;
    }

    @JsonAnyGetter
    Map<?, ?> getData() {
      return this.data;
    }
  }

  private static class Dependency {

    private final String from;
    private final String to;
    private final int numericFrom;
    private final int numericTo;
    @JsonIgnore
    private final Map<?, ?> data;

    Dependency(String from, int numericFrom, String to, int numericTo, Map<?, ?> data) {
      this.from = from;
      this.to = to;
      this.numericFrom = numericFrom;
      this.numericTo = numericTo;
      this.data = data;
    }

    @JsonAnyGetter
    Map<?, ?> getData() {
      return this.data;
    }

  }
}
