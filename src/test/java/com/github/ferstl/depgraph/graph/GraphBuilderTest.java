/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);
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

  @Test
  public void addNode() {
    // arrange
    this.graphBuilder.addNode(this.fromNode);

    // act
    this.graphBuilder.toString();

    // assert
    Node<?> fromNode = new Node<>(this.fromNode, "", "");
    assertThat(this.formatter.nodes, contains(new Node[]{fromNode}));
    assertThat(this.formatter.edges, Matchers.<Edge>empty());
  }

  @Test
  public void isEmpty() {
    // assert
    assertTrue(this.graphBuilder.isEmpty());
  }

  @Test
  public void isEmptyWithData() {
    // arrange
    this.graphBuilder.addEdge(this.fromNode, this.toNode);

    // assert
    assertFalse(this.graphBuilder.isEmpty());
  }

  @Test
  public void isReachable() {
    // arrange
    this.graphBuilder.addEdge("A", "B");
    this.graphBuilder.addEdge("B", "D");
    this.graphBuilder.addEdge("D", "E");
    this.graphBuilder.addEdge("A", "C");

    // assert
    assertTrue(this.graphBuilder.isReachable("E", "A"));
    assertTrue(this.graphBuilder.isReachable("D", "A"));
    assertTrue(this.graphBuilder.isReachable("C", "A"));
    assertTrue(this.graphBuilder.isReachable("B", "A"));

    assertFalse(this.graphBuilder.isReachable("C", "B"));
  }

  @Test
  public void isReachableWithCycle() {
    // arrange
    this.graphBuilder.addEdge("A", "B");
    this.graphBuilder.addEdge("B", "C");
    this.graphBuilder.addEdge("C", "A");

    // assert
    assertFalse(this.graphBuilder.isReachable("B", "X"));

    assertTrue(this.graphBuilder.isReachable("C", "A"));
    assertTrue(this.graphBuilder.isReachable("B", "A"));
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
