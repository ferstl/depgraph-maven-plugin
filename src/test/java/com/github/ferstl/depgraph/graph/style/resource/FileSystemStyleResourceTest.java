package com.github.ferstl.depgraph.graph.style.resource;

import java.nio.file.Paths;
import com.github.ferstl.depgraph.graph.style.resource.FileSystemStyleResource;


public class FileSystemStyleResourceTest extends AbstractStyleResourceTest {

  public FileSystemStyleResourceTest() {
    super(new FileSystemStyleResource(Paths.get("src/main/resources/default-style.json")), new FileSystemStyleResource(Paths.get("src/main/resources/does-not-exist")));
  }
}
