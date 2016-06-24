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
package com.github.ferstl.depgraph.dot;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.GraphNode;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.emptyGraph;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodes;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodesAndEdges;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for {@link DotBuilder}.
 */
public class DotBuilderTest {

  private static final String DEFAULT_FROM_NODE = "\"group:from:jar:1.0.0:compile\"[label=\"group:from:jar:1.0.0:compile\"]";
  private static final String DEFAULT_TO_NODE = "\"group:to:jar:1.0.0:compile\"[label=\"group:to:jar:1.0.0:compile\"]";
  private static final String DEFAULT_SINGLE_NODE = "\"group:start:jar:1.0.0:compile\"[label=\"group:start:jar:1.0.0:compile\"]";
  private static final String DEFAULT_EDGE = "\"group:from:jar:1.0.0:compile\" -> \"group:to:jar:1.0.0:compile\"";

  private DotBuilder<GraphNode> dotBuilder;
  private GraphNode fromNode;
  private GraphNode toNode;

  @Before
  public void before() {
    this.dotBuilder = new DotBuilder<>();
    this.fromNode = createNode("from");
    this.toNode = createNode("to");
  }

  @Test
  public void graphStructure() {
    String graph = this.dotBuilder.toString();
    assertThat(graph, startsWith("digraph \"G\" {"));
    assertThat(graph, endsWith("}"));
  }

  @Test
  public void graphName() {
    String graph = this.dotBuilder.graphName("test-graph").toString();
    assertThat(graph, startsWith("digraph \"test-graph\""));
  }

  @Test
  public void nodeStyle() {
    String graph = this.dotBuilder
        .nodeStyle(new AttributeBuilder()
            .shape("polygon")
            .addAttribute("sides", "6"))
        .toString();

    assertThat(graph, containsString("node [shape=\"polygon\",sides=\"6\"]"));
  }

  @Test
  public void edgeStyle() {
    String graph = this.dotBuilder
        .edgeStyle(new AttributeBuilder()
            .style("dotted")
            .fontName("Courier italic")
            .fontSize(10))
        .toString();

    assertThat(graph, containsString("edge [style=\"dotted\",fontname=\"Courier italic\",fontsize=\"10\"]"));
  }

  @Test
  public void defaults() {
    this.dotBuilder.addEdge(this.fromNode, this.toNode);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE}));

  }

  @Test
  public void nullNodes() {
    GraphNode node = createNode("node");

    this.dotBuilder.addEdge(node, null);
    assertThat(this.dotBuilder, emptyGraph());

    this.dotBuilder.addEdge(null, node);
    assertThat(this.dotBuilder, emptyGraph());
  }

  @Test
  public void omitSelfReferences() {
    this.dotBuilder
        .omitSelfReferences()
        .addEdge(createNode("start"), createNode("start"));

    assertThat(this.dotBuilder, hasNodes(DEFAULT_SINGLE_NODE));

    this.dotBuilder.addEdge(this.fromNode, this.toNode);
    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_SINGLE_NODE, DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE}));
  }


  @Test
  public void customNodeRenderer() {
    this.dotBuilder.useNodeRenderer(TestRenderer.INSTANCE);

    this.dotBuilder.addEdge(this.fromNode, this.toNode);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"from\"",
            "\"to\""},
        new String[]{"from -> to"}));
  }

  @Test
  public void customNodeAttributeRenderer() {
    this.dotBuilder.useNodeAttributeRenderer(TestRenderer.INSTANCE);

    this.dotBuilder.addEdge(this.fromNode, this.toNode);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"group:from:jar:1.0.0:compile\"[label=\"from\"]",
            "\"group:to:jar:1.0.0:compile\"[label=\"to\"]"},
        new String[]{"from -> to"}));
  }


  @Test
  public void customEdgeRenderer() {
    this.dotBuilder
        .useEdgeRenderer(TestRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE + "[label=\"1.0.0\"]"}));
  }

  @Test
  public void addEdgeWithCustomRenderer() {
    this.dotBuilder.addEdge(this.fromNode, this.toNode, TestRenderer.INSTANCE);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{DEFAULT_FROM_NODE, DEFAULT_TO_NODE},
        new String[]{DEFAULT_EDGE + "[label=\"1.0.0\"]"}));

  }

  @Test
  public void customNodeLabelRenderer() {
    this.dotBuilder
        .useNodeAttributeRenderer(TestRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"group:from:jar:1.0.0:compile\"[label=\"from\"]",
            "\"group:to:jar:1.0.0:compile\"[label=\"to\"]"},
        new String[]{DEFAULT_EDGE}));
  }


  private GraphNode createNode(String name) {
    Artifact artifact = new DefaultArtifact("group", name, "1.0.0", "compile", "jar", "", null);

    return new GraphNode(artifact);
  }

  enum TestRenderer implements EdgeRenderer<GraphNode>, NodeRenderer<GraphNode>, NodeAttributeRenderer<GraphNode> {
    INSTANCE;

    @Override
    public String render(GraphNode node) {
      return node.getArtifact().getArtifactId();
    }

    @Override
    public String createEdgeAttributes(GraphNode from, GraphNode to) {
      return new AttributeBuilder()
          .label(to.getArtifact().getVersion())
          .toString();
    }

    @Override
    public String createNodeAttributes(GraphNode node) {
      return new AttributeBuilder()
          .label(node.getArtifact().getArtifactId())
          .toString();
    }

  }
}
