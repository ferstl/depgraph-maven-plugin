/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for {@link GraphBuilder}.
 */
class GraphBuilderTest {

  private GraphBuilder<String> graphBuilder;
  private String fromNode;
  private String toNode;
  private TestFormatter formatter;

  @BeforeEach
  void before() {
    this.formatter = new TestFormatter();
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);
    this.graphBuilder.graphFormatter(this.formatter);

    this.fromNode = "from";
    this.toNode = "to";
  }

  @Test
  void graphName() {
    // arrange
    this.graphBuilder.graphName("test-graph");

    // act
    this.graphBuilder.toString();

    // assert
    assertEquals(this.formatter.graphName, "test-graph");
  }


  @Test
  void defaults() {
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
  void nullNodes() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, null);

    // act
    this.graphBuilder.toString();

    // assert
    assertThat(this.formatter.nodes, empty());
    assertThat(this.formatter.edges, empty());
  }

  @Test
  void dontOmitSelfReferences() {
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
  void omitSelfReferences() {
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
  void customNodeNameRenderer() {
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
  void customEdgeRenderer() {
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
  void getEffectiveNode() {
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
  void getEffectiveNodeForUnknownNode() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    // act
    String effectiveNode = this.graphBuilder.getEffectiveNode("somethingCompletelyDifferent");

    // assert
    assertEquals("somethingCompletelyDifferent", effectiveNode);
  }

  @Test
  void addNode() {
    // arrange
    this.graphBuilder.addNode(this.fromNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode, "", "");
    assertThat(this.formatter.nodes, contains(new Node[]{fromNode}));
    assertThat(this.formatter.edges, empty());
  }

  @Test
  void isEmpty() {
    // assert
    assertTrue(this.graphBuilder.isEmpty());
  }

  @Test
  void isEmptyWithData() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    // assert
    assertFalse(this.graphBuilder.isEmpty());
  }

  @Test
  void reduceEdges() {
    // arrange
    this.graphBuilder.addEdge("A", "B");
    this.graphBuilder.addEdge("B", "C");
    this.graphBuilder.addEdge("A", "C");

    // act
    this.graphBuilder.reduceEdges();
    this.graphBuilder.toString();

    // assert
    // A -> C is redundant
    assertThat(this.formatter.edges, containsInAnyOrder(
        new Edge("A", "B", ""),
        new Edge("B", "C", "")));
  }

  @Test
  void reduceEdgesWithPermanentEdge() {
    // arrange
    this.graphBuilder.addEdge("A", "B");
    this.graphBuilder.addEdge("B", "C");
    this.graphBuilder.addPermanentEdge("A", "C");

    // act
    this.graphBuilder.reduceEdges();
    this.graphBuilder.toString();

    // assert
    // A -> C is permanen t
    assertThat(this.formatter.edges, containsInAnyOrder(
        new Edge("A", "B", ""),
        new Edge("B", "C", ""),
        new Edge("A", "C", "")));
  }

  @Test
  void reduceEdgesWithCycle() {
    // arrange
    this.graphBuilder.addEdge("A", "B");
    this.graphBuilder.addEdge("B", "C");
    this.graphBuilder.addEdge("A", "C");
    this.graphBuilder.addEdge("C", "A");

    // act
    this.graphBuilder.reduceEdges();
    this.graphBuilder.toString();

    // assert
    // A -> C is redundant
    assertThat(this.formatter.edges, containsInAnyOrder(
        new Edge("A", "B", ""),
        new Edge("B", "C", ""),
        new Edge("C", "A", "")));
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
