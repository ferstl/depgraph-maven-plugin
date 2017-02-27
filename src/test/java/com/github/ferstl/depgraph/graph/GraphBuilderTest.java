/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph.graph;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;
import com.github.ferstl.depgraph.graph.dot.DotGraphFormatter;

import static com.github.ferstl.depgraph.graph.DotBuilderMatcher.emptyGraph;
import static com.github.ferstl.depgraph.graph.DotBuilderMatcher.hasNodes;
import static com.github.ferstl.depgraph.graph.DotBuilderMatcher.hasNodesAndEdges;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for {@link GraphBuilder}.
 */
public class GraphBuilderTest {

  private static final String DEFAULT_FROM_NODE = "\"group:from:jar:1.0.0:compile\"[label=\"group:from:jar:1.0.0:compile\"]";
  private static final String DEFAULT_TO_NODE = "\"group:to:jar:1.0.0:compile\"[label=\"group:to:jar:1.0.0:compile\"]";
  private static final String DEFAULT_SINGLE_NODE = "\"group:start:jar:1.0.0:compile\"[label=\"group:start:jar:1.0.0:compile\"]";
  private static final String DEFAULT_EDGE = "\"group:from:jar:1.0.0:compile\" -> \"group:to:jar:1.0.0:compile\"";

  private GraphBuilder<DependencyNode> graphBuilder;
  private DependencyNode fromNode;
  private DependencyNode toNode;

  @Before
  public void before() {
    this.graphBuilder = new GraphBuilder<>();
    this.fromNode = createNode("from");
    this.toNode = createNode("to");
  }

  @Test
  public void graphStructure() {
    String graph = this.graphBuilder.toString();
    assertThat(graph, startsWith("digraph \"G\" {"));
    assertThat(graph, endsWith("}"));
  }

  @Test
  public void graphName() {
    String graph = this.graphBuilder.graphName("test-graph").toString();
    assertThat(graph, startsWith("digraph \"test-graph\""));
  }

  // TODO: #19 - Move test
  @Test
  public void nodeStyle() {
    DotAttributeBuilder nodeStyle = new DotAttributeBuilder()
        .shape("polygon")
        .addAttribute("sides", "6");
    String graph = this.graphBuilder
        .graphFormatter(new DotGraphFormatter(nodeStyle, new DotAttributeBuilder()))
        .toString();

    assertThat(graph, containsString("node [shape=\"polygon\",sides=\"6\"]"));
  }

  // TODO: #19 - Move test
  @Test
  public void edgeStyle() {
    DotAttributeBuilder edgeStyle = new DotAttributeBuilder()
        .style("dotted")
        .fontName("Courier italic")
        .fontSize(10);

    String graph = this.graphBuilder
        .graphFormatter(new DotGraphFormatter(new DotAttributeBuilder(), edgeStyle))
        .toString();

    assertThat(graph, containsString("edge [style=\"dotted\",fontname=\"Courier italic\",fontsize=\"10\"]"));
  }

  @Test
  public void defaults() {
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE}));

  }

  @Test
  public void nullNodes() {
    DependencyNode node = createNode("node");

    this.graphBuilder.addEdge(node, null);
    assertThat(this.graphBuilder, emptyGraph());

    this.graphBuilder.addEdge(null, node);
    assertThat(this.graphBuilder, emptyGraph());
  }

  @Test
  public void omitSelfReferences() {
    this.graphBuilder
        .omitSelfReferences()
        .addEdge(createNode("start"), createNode("start"));

    assertThat(this.graphBuilder, hasNodes(DEFAULT_SINGLE_NODE));

    this.graphBuilder.addEdge(this.fromNode, this.toNode);
    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_SINGLE_NODE, DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE}));
  }


  @Test
  public void customNodeIdRenderer() {
    this.graphBuilder.useNodeIdRenderer(TestNodeIdRenderer.INSTANCE);

    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"from\"",
            "\"to\""},
        new String[]{"from -> to"}));
  }

  @Test
  public void customNodeNameRenderer() {
    this.graphBuilder.useNodeNameRenderer(TestNodeNameRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"group:from:jar:1.0.0:compile\"[label=\"from\"]",
            "\"group:to:jar:1.0.0:compile\"[label=\"to\"]"},
        new String[]{DEFAULT_TO_NODE}));
  }


  @Test
  public void customEdgeRenderer() {
    this.graphBuilder
        .useEdgeRenderer(TestEdgeRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE + "[label=\"1.0.0\"]"}));
  }

  private DependencyNode createNode(String name) {
    Artifact artifact = new DefaultArtifact("group", name, "1.0.0", "compile", "jar", "", null);

    return new DependencyNode(artifact);
  }

  enum TestNodeIdRenderer implements NodeRenderer<DependencyNode> {
    INSTANCE;

    @Override
    public String render(DependencyNode node) {
      return node.getArtifact().getArtifactId();
    }
  }

  enum TestNodeNameRenderer implements NodeRenderer<DependencyNode> {
    INSTANCE;

    @Override
    public String render(DependencyNode node) {
      return new DotAttributeBuilder()
          .label(node.getArtifact().getArtifactId())
          .toString();
    }

  }

  enum TestEdgeRenderer implements EdgeRenderer<DependencyNode> {
    INSTANCE;

    @Override
    public String render(DependencyNode from, DependencyNode to) {
      return new DotAttributeBuilder()
          .label(to.getArtifact().getVersion())
          .toString();
    }

  }
}
