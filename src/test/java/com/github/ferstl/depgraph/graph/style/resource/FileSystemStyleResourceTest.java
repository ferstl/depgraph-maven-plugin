package com.github.ferstl.depgraph.graph.style.resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class FileSystemStyleResourceTest extends AbstractStyleResourceTest {

  public FileSystemStyleResourceTest() {
    super(new FileSystemStyleResource(Paths.get("src/main/resources/default-style.json")), new FileSystemStyleResource(Paths.get("src/main/resources/does-not-exist")));
  }

  @Test
  public void toStringTest() {
    Path path = Paths.get("src/main/resources/default-style.json");
    assertEquals("file:" + path.toString(), this.existingResource.toString());
  }
}
