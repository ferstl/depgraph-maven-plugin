package com.github.ferstl.depgraph.graph.style.resource;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClasspathStyleResourceTest extends AbstractStyleResourceTest {

  public ClasspathStyleResourceTest() {
    super(
        new ClasspathStyleResource("default-style.json", ClasspathStyleResourceTest.class.getClassLoader()),
        new ClasspathStyleResource("does-not-exist", ClasspathStyleResourceTest.class.getClassLoader()));
  }

  @Test
  public void toStringTest() {
    assertEquals("classpath:default-style.json", this.existingResource.toString());
  }
}
