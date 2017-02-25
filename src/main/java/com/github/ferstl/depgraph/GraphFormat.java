package com.github.ferstl.depgraph;

public enum GraphFormat {
  DOT, GML;

  public static GraphFormat forName(String name) {
    try {
      return valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unsupported output format: " + name, e);
    }
  }

  public String getFileExtension() {
    return "." + name().toLowerCase();
  }
}
