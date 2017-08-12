package com.github.ferstl.depgraph.graph.json;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class Artifact {

  private final String id;
  private final int numericId;

  @JsonRawValue
  private final String data;

  public Artifact(String id, int numericId, String data) {
    this.id = id;
    this.numericId = numericId;
    this.data = data;
  }
}
