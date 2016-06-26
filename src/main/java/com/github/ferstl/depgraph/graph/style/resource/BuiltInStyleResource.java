package com.github.ferstl.depgraph.graph.style.resource;


public enum BuiltInStyleResource {

  DEFAULT_STYLE("default-style.json"),
  GROUP_ID_ONLY_STYLE("group-id-only-style.json");

  private final String styleConfig;

  BuiltInStyleResource(String styleConfig) {
    this.styleConfig = styleConfig;
  }

  public ClasspathStyleResource createStyleResource(ClassLoader classLoader) {
    return new ClasspathStyleResource(this.styleConfig, classLoader);
  }
}
