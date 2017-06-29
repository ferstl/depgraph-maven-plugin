package com.github.ferstl.depgraph.dependency;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gushakov
 */
public class PumlDependencyEgdeRendererTest {

    private PumlDependencyEgdeRenderer renderer = new PumlDependencyEgdeRenderer();

    @Test
    public void testRenderEdgeToIncludeDependency() throws Exception {
        final DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
                "spring-context", "4.3.9-RELEASE");

        final DependencyNode toNode = DependencyNodeUtil.createDependencyNode("org.springframework",
                "spring-core", "4.3.9-RELEASE");

        final String edgeInfo = renderer.render(fromNode, toNode);

        assertEquals("{\"begin\":\"-[\",\"end\":\"]->\",\"color\":\"#000000\",\"label\":\"\"}", edgeInfo);

    }

    @Test
    public void testRenderEdgeToConflictingDependency() throws Exception {
        final DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
                "spring-context", "4.3.9-RELEASE");

        final DependencyNode toNode = DependencyNodeUtil.createDependencyNodeWithConflict("commons-logging",
                "commons-logging", "1.1.3");

        final String edgeInfo = renderer.render(fromNode, toNode);

        assertEquals("{\"begin\":\".[\",\"end\":\"].>\",\"color\":\"#FF0000\",\"label\":\"1.1.3\"}", edgeInfo);
    }
}