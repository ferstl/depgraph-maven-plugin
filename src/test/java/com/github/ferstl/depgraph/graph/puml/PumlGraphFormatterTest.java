package com.github.ferstl.depgraph.graph.puml;

import com.github.ferstl.depgraph.dependency.*;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author gushakov
 */
public class PumlGraphFormatterTest {

    private class Tuple {
        private String description;
        private boolean conflict = false;

        Tuple(String description, boolean conflict) {
            this.description = description;
            this.conflict = conflict;
        }

        String getDescription() {
            return description;
        }

        boolean isConflict() {
            return conflict;
        }
    }

    private PumlGraphFormatter formatter = new PumlGraphFormatter();

    private NodeRenderer<DependencyNode> nodeIdRenderer = NodeIdRenderers.VERSIONLESS_ID;

    private PumlDependencyNodeNameRenderer nodeInfoRenderer = new PumlDependencyNodeNameRenderer(true, true, true);

    private PumlDependencyEgdeRenderer edgeInfoRenderer = new PumlDependencyEgdeRenderer();

    private List<Tuple> dependencies = Arrays.asList(
         /* 0 */   new Tuple("com.github.ferstl:depgraph-maven-plugin:2.2.1-SNAPSHOT:compile", false),
         /* 1 */   new Tuple("com.fasterxml.jackson.core:jackson-databind:2.8.7:compile", false),
         /* 2 */   new Tuple("com.google.guava:guava:21.0:compile", false),
         /* 3 */   new Tuple("org.apache.maven:maven-core:jar:3.3.9:provided", false),
         /* 4 */   new Tuple("com.google.inject:guice:4.0:provided", false),
         /* 5 */   new Tuple("com.google.guava:guava:16.0.1:provided", true),
         /* 6 */   new Tuple("junit:junit:4.12:test", false)
    );

    private List<Node<?>> nodes = Lists.transform(dependencies,
            new Function<Tuple, Node<?>>() {
                @Override
                public Node<?> apply(Tuple tuple) {
                    return makeNode(tuple.description, tuple.conflict);
                }
            });

    private Collection<Edge> edges = Arrays.asList(
            makeEgde(dependencies.get(0), dependencies.get(1)),
            makeEgde(dependencies.get(0), dependencies.get(2)),
            makeEgde(dependencies.get(0), dependencies.get(3)),
            makeEgde(dependencies.get(0), dependencies.get(6)),
            makeEgde(dependencies.get(3), dependencies.get(4)),
            makeEgde(dependencies.get(4), dependencies.get(5))
    );

    @Test
    public void testFormatDependenciesGraphAsPumlDiagram() throws Exception {
        final String puml = formatter.format("graphName", nodes, edges);
        assertEquals("@startuml\n" +
                "skinparam rectangle {\n" +
                "  BackgroundColor<<test>> lightGreen\n" +
                "  BackgroundColor<<runtime>> lightBlue\n" +
                "  BackgroundColor<<provided>> lightGray\n" +
                "}\n" +
                "rectangle \"com.github.ferstl:depgraph-maven-plugin:2.2.1-SNAPSHOT\" as com_github_ferstl_depgraph_maven_plugin_\n" +
                "rectangle \"com.fasterxml.jackson.core:jackson-databind:2.8.7\" as com_fasterxml_jackson_core_jackson_databind_\n" +
                "rectangle \"com.google.guava:guava:21.0\" as com_google_guava_guava_\n" +
                "rectangle \"org.apache.maven:maven-core:jar\" as org_apache_maven_maven_core_<<3.3.9>>\n" +
                "rectangle \"com.google.inject:guice:4.0\" as com_google_inject_guice_<<provided>>\n" +
                "rectangle \"com.google.guava:guava:16.0.1\" as com_google_guava_guava_<<provided>>\n" +
                "rectangle \"junit:junit:4.12\" as junit_junit_<<test>>\n" +
                "com_github_ferstl_depgraph_maven_plugin_ -[#000000]-> com_fasterxml_jackson_core_jackson_databind_\n" +
                "com_github_ferstl_depgraph_maven_plugin_ -[#000000]-> com_google_guava_guava_\n" +
                "com_github_ferstl_depgraph_maven_plugin_ -[#000000]-> org_apache_maven_maven_core_\n" +
                "com_github_ferstl_depgraph_maven_plugin_ -[#000000]-> junit_junit_\n" +
                "org_apache_maven_maven_core_ -[#000000]-> com_google_inject_guice_\n" +
                "com_google_inject_guice_ .[#FF0000].> com_google_guava_guava_: 16.0.1\n" +
                "@enduml", puml);
    }

    private Node<?> makeNode(String description, boolean conflict) {

        final DependencyNode dependencyNode = makeDependencyNode(description, conflict);

        final String nodeId = nodeIdRenderer.render(dependencyNode);

        final String nodeInfo = nodeInfoRenderer.render(dependencyNode);

        return new Node<>(nodeId, nodeInfo, new Object());

    }

    private DependencyNode makeDependencyNode(String description, boolean conflict) {
        final String[] parts = description.split(":");

        return conflict
                ? DependencyNodeUtil.createDependencyNodeWithConflict(parts[0], parts[1], parts[2], parts[3])
                : DependencyNodeUtil.createDependencyNode(parts[0], parts[1], parts[2], parts[3]);

    }

    private Edge makeEgde(Tuple from, Tuple to) {
        final DependencyNode fromNode = makeDependencyNode(from.getDescription(), from.isConflict());

        final DependencyNode toNode = makeDependencyNode(to.getDescription(), to.isConflict());

        return new Edge(nodeIdRenderer.render(fromNode),
                nodeIdRenderer.render(toNode),
                edgeInfoRenderer.render(fromNode, toNode));
    }
}