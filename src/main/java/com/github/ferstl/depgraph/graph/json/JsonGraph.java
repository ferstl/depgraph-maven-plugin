package com.github.ferstl.depgraph.graph.json;

import java.util.ArrayList;
import java.util.List;

class JsonGraph {

  private final List<Artifact> artifacts = new ArrayList<>();
  private final List<Dependency> dependencies = new ArrayList<>();

  void addArtifact(Artifact artifact) {
    this.artifacts.add(artifact);
  }

  void addDependency(Dependency dependency) {
    this.dependencies.add(dependency);
  }
}
