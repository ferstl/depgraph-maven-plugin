package com.github.ferstl.depgraph.graph.style.resource;

import com.github.ferstl.depgraph.graph.style.resource.ClasspathStyleResource;

public class ClasspathStyleResourceTest extends AbstractStyleResourceTest {

  public ClasspathStyleResourceTest() {
    super(
        new ClasspathStyleResource("default-style.json", ClasspathStyleResourceTest.class.getClassLoader()),
        new ClasspathStyleResource("does-not-exist", ClasspathStyleResourceTest.class.getClassLoader()));
  }
}
