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

import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for {@link GraphBuilder}.
 */
public class GraphBuilderTest {

  private GraphBuilder<String> graphBuilder;
  private String fromNode;
  private String toNode;
  private TestFormatter formatter;

  @Before
  public void before() {
    this.formatter = new TestFormatter();
    this.graphBuilder = GraphBuilder.create();
    this.graphBuilder.graphFormatter(this.formatter);

    this.fromNode = "from";
    this.toNode = "to";
  }

  @Test
  public void graphName() {
    // arrange
    this.graphBuilder.graphName("test-graph");

    // act
    this.graphBuilder.toString();

    // assert
    assertEquals(this.formatter.graphName, "test-graph");
  }


  @Test
  public void defaults() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode, "", "");
    Node<?> toNode = new Node<>(this.toNode, "", "");

    assertThat(this.formatter.nodes, containsInAnyOrder(fromNode, toNode));
    assertThat(this.formatter.edges, contains(new Edge(this.fromNode, this.toNode, "")));

  }

  @Test
  public void nullNodes() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, null);

    // act
    this.graphBuilder.toString();

    // assert
    assertThat(this.formatter.nodes, empty());
    assertThat(this.formatter.edges, empty());
  }

  @Test
  public void dontOmitSelfReferences() {
    // arrange
    this.graphBuilder
        .addEdge(this.fromNode, this.fromNode);


    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>("from", "", "");
    assertEquals(this.formatter.nodes, singletonList(fromNode));
    assertThat(this.formatter.edges, contains(new Edge(this.fromNode, this.fromNode, "")));
  }

  @Test
  public void omitSelfReferences() {
    // arrange
    this.graphBuilder
        .omitSelfReferences()
        .addEdge(this.fromNode, this.fromNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>("from", "", "");
    assertEquals(this.formatter.nodes, singletonList(fromNode));
    assertThat(this.formatter.edges, empty());
  }


  @Test
  public void customNodeIdRenderer() {
    // arrange
    this.graphBuilder.useNodeIdRenderer(TestNodeRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode + "-custom", "", "");
    Node<?> toNode = new Node<>(this.toNode + "-custom", "", "");
    assertThat(this.formatter.nodes, containsInAnyOrder(fromNode, toNode));
    assertThat(this.formatter.edges, contains(new Edge(this.fromNode + "-custom", this.toNode + "-custom", "")));
  }


  @Test
  public void customNodeNameRenderer() {
    // arrange
    this.graphBuilder.useNodeNameRenderer(TestNodeRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode, this.fromNode + "-custom", "");
    Node<?> toNode = new Node<>(this.toNode, this.toNode + "-custom", "");
    assertThat(this.formatter.nodes, containsInAnyOrder(fromNode, toNode));
    assertThat(this.formatter.edges, contains(new Edge(this.fromNode, this.toNode, "")));
  }


  @Test
  public void customEdgeRenderer() {
    // arrange
    this.graphBuilder
        .useEdgeRenderer(TestEdgeRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode, "", "");
    Node<?> toNode = new Node<>(this.toNode, "", "");
    assertThat(this.formatter.nodes, containsInAnyOrder(fromNode, toNode));
    assertThat(this.formatter.edges, contains(new Edge(this.fromNode, this.toNode, "f->t")));
  }

  @Test
  public void getEffectiveNode() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);
    this.graphBuilder.useNodeNameRenderer(TestNodeRenderer.INSTANCE)
        .addEdge(this.fromNode, this.toNode);

    // act
    String effectiveNode = this.graphBuilder.getEffectiveNode(this.fromNode);

    // assert
    assertEquals(this.fromNode, effectiveNode);
  }

  @Test
  public void getEffectiveNodeForUnknownNode() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    // act
    String effectiveNode = this.graphBuilder.getEffectiveNode("somethingCompletelyDifferent");

    // assert
    assertEquals("somethingCompletelyDifferent", effectiveNode);
  }

  enum TestNodeRenderer implements NodeRenderer<String> {
    INSTANCE;

    @Override
    public String render(String node) {
      return node + "-custom";
    }
  }


  enum TestEdgeRenderer implements EdgeRenderer<String> {
    INSTANCE;

    @Override
    public String render(String from, String to) {
      return from.substring(0, 1) + "->" + to.substring(0, 1);
    }
  }
}
