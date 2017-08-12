package com.github.ferstl.depgraph.graph.json;

import java.util.ArrayList;
import java.util.List;

public class JsonGraph {

  private final List<Artifact> artifacts = new ArrayList<>();
  private final List<Dependency> dependencies = new ArrayList<>();

  public void addArtifact(Artifact artifact) {
    this.artifacts.add(artifact);
  }

  public void addDependency(Dependency dependency) {
    this.dependencies.add(dependency);
  }
}
