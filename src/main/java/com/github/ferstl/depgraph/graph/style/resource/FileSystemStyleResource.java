package com.github.ferstl.depgraph.graph.style.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class FileSystemStyleResource implements StyleResource {

  private final Path location;

  public FileSystemStyleResource(Path location) {
    this.location = location;
  }

  @Override
  public boolean exists() {
    return Files.exists(this.location);
  }

  @Override
  public InputStream openStream() throws IOException {
    return Files.newInputStream(this.location, StandardOpenOption.READ);
  }

  @Override
  public String toString() {
    return "file:" + this.location;
  }
}
