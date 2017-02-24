package com.github.ferstl.depgraph;

public enum OutputFormat {
  DOT, GML;

  public static OutputFormat forName(String name) {
    try {
      return valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unsupported output format: " + name, e);
    }
  }
}
