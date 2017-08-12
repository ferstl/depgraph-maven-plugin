package com.github.ferstl.depgraph.graph.json;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonRawValue;

class JsonGraph {

  private final List<Artifact> artifacts = new ArrayList<>();
  private final List<Dependency> dependencies = new ArrayList<>();

  void addArtifact(String nodeId, int numericNodeId, String nodeData) {
    this.artifacts.add(new Artifact(nodeId, numericNodeId, nodeData));
  }

  void addDependency(String fromNodeId, int fromNodeIdNumeric, String toNodeId, int toNodeIdNumeric, String data) {
    this.dependencies.add(new Dependency(fromNodeId, fromNodeIdNumeric, toNodeId, toNodeIdNumeric, data));
  }

  private static class Artifact {

    private final String id;
    private final int numericId;

    @JsonRawValue
    private final String data;

    Artifact(String id, int numericId, String data) {
      this.id = id;
      this.numericId = numericId;
      this.data = data;
    }
  }

  private static class Dependency {

    private final String from;
    private final String to;
    private final int numericFrom;
    private final int numericTo;

    @JsonRawValue
    private final String data;

    Dependency(String from, int numericFrom, String to, int numericTo, String data) {
      this.from = from;
      this.to = to;
      this.numericFrom = numericFrom;
      this.numericTo = numericTo;
      this.data = data;
    }

  }
}
