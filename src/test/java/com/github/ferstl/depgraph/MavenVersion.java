package com.github.ferstl.depgraph;

public final class MavenVersion {

  public static final String MIN_VERSION = "3.1.0";
  public static final String MAX_VERSION = "3.6.1";

  private MavenVersion() {
    throw new AssertionError("not instantiable");
  }
}
