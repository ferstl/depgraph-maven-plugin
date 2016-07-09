package com.github.ferstl.depgraph.graph.style;

import org.apache.commons.lang3.StringUtils;
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
      expanded[i] = StringUtils.defaultIfEmpty(parts[i], null);
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

  public static StyleKey create(String groupId, String artifactId, String scope, String type, String version) {
    return new StyleKey(new String[]{groupId, artifactId, scope, type, version});
  }

  public boolean matches(StyleKey other) {
    return (this.groupId == null || wildCardMatch(this.groupId, other.groupId))
        && (this.artifactId == null || wildCardMatch(this.artifactId, other.artifactId))
        && (this.scope == null || match(this.scope, other.scope))
        && (this.type == null || match(this.type, other.type))
        && (this.version == null || wildCardMatch(this.version, other.version));

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

  @Override
  public String toString() {
    return Joiner.on(",").useForNull("").join(this.groupId, this.artifactId, this.scope, this.type, this.version);
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

  private static boolean wildCardMatch(String value1, String value2) {
    if (StringUtils.endsWith(value1, "*")) {
      return StringUtils.startsWith(value2, value1.substring(0, value1.length() - 1));
    }

    return match(value1, value2);
  }

  private static boolean match(String value1, String value2) {
    return StringUtils.equals(value1, value2);
  }

}
