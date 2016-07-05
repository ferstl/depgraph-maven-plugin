package com.github.ferstl.depgraph.graph.style;

import com.google.common.base.Joiner;

public final class StyleKey implements Comparable<StyleKey> {

  private static final int NUM_ELEMENTS = 5;

  String groupId;
  String artifactId;
  String scope;
  String type;
  String version;


  private StyleKey(String[] parts) {
    if (parts.length > NUM_ELEMENTS) {
      throw new IllegalArgumentException("Too many parts. Expecting '<groupId>:<artifactId>:<version>:<scope>:<type>'");
    }

    String[] expanded = new String[NUM_ELEMENTS];
    for (int i = 0; i < parts.length; i++) {
      expanded[i] = parts[i];
    }

    this.groupId = expanded[0];
    this.artifactId = expanded[1];
    this.scope = expanded[2];
    this.type = expanded[3];
    this.version = expanded[4];
  }

  public static StyleKey fromString(String keyString) {
    String[] parts = keyString.split(",");
    return new StyleKey(parts);
  }

  @Override
  public int compareTo(StyleKey o) {
    return getRank() - o.getRank();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof StyleKey)) {
      return false;
    }

    StyleKey other = (StyleKey) obj;

    return compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    return getRank();
  }

  private int getRank() {
    int rank = 0;

    rank += this.groupId != null ? 16 : 0;
    rank += this.artifactId != null ? 8 : 0;
    rank += this.scope != null ? 4 : 0;
    rank += this.type != null ? 2 : 0;
    rank += this.version != null ? 1 : 0;

    return rank;
  }

  @Override
  public String toString() {
    return Joiner.on(",").useForNull("").join(this.groupId, this.artifactId, this.scope, this.type, this.version);
  }

}
