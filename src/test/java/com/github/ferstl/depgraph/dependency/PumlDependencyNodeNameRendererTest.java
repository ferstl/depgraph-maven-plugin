package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PumlDependencyNodeNameRendererTest {


  private final PumlDependencyNodeNameRenderer renderer = new PumlDependencyNodeNameRenderer(true, true, true);

  @Test
  public void testRenderCompileNode() throws Exception {

    final DependencyNode dependencyNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-context", "4.3.9.RELEASE");

    String nodeInfo = this.renderer.render(dependencyNode);

    assertEquals("{\"component\":\"rectangle\",\"label\":\"org.springframework:spring-context:4.3.9.RELEASE\",\"stereotype\":\"compile\"}",
        nodeInfo);

  }
}
