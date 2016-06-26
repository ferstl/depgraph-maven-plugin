package com.github.ferstl.depgraph.graph.style.resource;

import java.io.IOException;
import java.io.InputStream;


public class ClasspathStyleResource implements StyleResource {

  private final String name;
  private final ClassLoader classLoader;

  public ClasspathStyleResource(String name, ClassLoader classLoader) {
    this.name = name;
    this.classLoader = classLoader;
  }

  @Override
  public boolean exists() {
    return this.classLoader.getResource(this.name) != null;
  }

  @Override
  public InputStream openStream() throws IOException {
    InputStream inputStream = this.classLoader.getResourceAsStream(this.name);
    if (inputStream == null) {
      throw new IOException("Resource " + this.name + " does not exist.");
    }

    return inputStream;
  }

}
