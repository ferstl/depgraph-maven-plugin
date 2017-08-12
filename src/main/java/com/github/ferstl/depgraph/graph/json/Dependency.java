package com.github.ferstl.depgraph.graph.json;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class Dependency {

  private final String from;
  private final String to;
  private final int numericFrom;
  private final int numericTo;
  @JsonRawValue
  private final String data;

  public Dependency(String from, int numericFrom, String to, int numericTo, String data) {
    this.from = from;
    this.to = to;
    this.numericFrom = numericFrom;
    this.numericTo = numericTo;
    this.data = data;
  }
}
