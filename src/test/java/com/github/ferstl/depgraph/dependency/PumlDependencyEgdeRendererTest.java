package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PumlDependencyEgdeRendererTest {

  private final PumlDependencyEgdeRenderer renderer = new PumlDependencyEgdeRenderer();

  @Test
  public void testRenderEdgeToIncludeDependency() throws Exception {
    DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-context", "4.3.9-RELEASE");

    DependencyNode toNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-core", "4.3.9-RELEASE");

    String edgeInfo = this.renderer.render(fromNode, toNode);

    assertEquals("{\"begin\":\"-[\",\"end\":\"]->\",\"color\":\"#000000\",\"label\":\"\"}", edgeInfo);

  }

  @Test
  public void testRenderEdgeToConflictingDependency() throws Exception {
    DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-context", "4.3.9-RELEASE");

    DependencyNode toNode = DependencyNodeUtil.createDependencyNodeWithConflict("commons-logging",
        "commons-logging", "1.1.3");

    String edgeInfo = this.renderer.render(fromNode, toNode);

    assertEquals("{\"begin\":\".[\",\"end\":\"].>\",\"color\":\"#FF0000\",\"label\":\"1.1.3\"}", edgeInfo);
  }
}
