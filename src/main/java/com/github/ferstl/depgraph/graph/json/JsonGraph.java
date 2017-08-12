package com.github.ferstl.depgraph.graph.json;

import java.util.ArrayList;
import java.util.List;

class JsonGraph {

  private final List<Artifact> artifacts = new ArrayList<>();
  private final List<Dependency> dependencies = new ArrayList<>();

  void addArtifact(String nodeId, int numericNodeId, String nodeData) {
    this.artifacts.add(new Artifact(nodeId, numericNodeId, nodeData));
  }

  void addDependency(String fromNodeId, int fromNodeIdNumeric, String toNodeId, int toNodeIdNumeric, String data) {
    this.dependencies.add(new Dependency(fromNodeId, fromNodeIdNumeric, toNodeId, toNodeIdNumeric, data));
  }
}
