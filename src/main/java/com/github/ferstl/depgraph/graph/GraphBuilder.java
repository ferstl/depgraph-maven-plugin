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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;
import com.github.ferstl.depgraph.graph.dot.DotGraphFormatter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * A builder to create <a href="http://www.graphviz.org/doc/info/lang.html">DOT</a> strings by defining edges between
 * Nodes. The builder allows some customizations including custom {@link NodeRenderer}s and
 * {@link EdgeRenderer}s.
 *
 * @param <T> Type of the graph nodes.
 */
public final class GraphBuilder<T> {

  private String graphName;
  private GraphFormatter graphFormatter;
  private NodeRenderer<? super T> nodeIdRenderer;
  private NodeRenderer<? super T> nodeNameRenderer;
  private EdgeRenderer<? super T> edgeRenderer;
  private boolean omitSelfReferences;
  private final Map<String, Node<T>> nodeDefinitions;
  private final Set<Edge> edges;

  public static <T> GraphBuilder<T> create() {
    return new GraphBuilder<>();
  }

  public GraphBuilder() {
    DotAttributeBuilder nodeAttributeBuilder = new DotAttributeBuilder().shape("box").fontName("Helvetica");
    DotAttributeBuilder edgeAttributeBuilder = new DotAttributeBuilder().fontName("Helvetica").fontSize(10);

    this.graphName = "G";
    this.graphFormatter = new DotGraphFormatter(nodeAttributeBuilder, edgeAttributeBuilder);
    this.nodeIdRenderer = createDefaultNodeIdRenderer();
    this.nodeNameRenderer = createDefaultNodeNameRenderer();
    this.edgeRenderer = createDefaultEdgeRenderer();

    this.nodeDefinitions = new LinkedHashMap<>();
    this.edges = new LinkedHashSet<>();
  }

  public GraphBuilder<T> graphName(String name) {
    this.graphName = name;
    return this;
  }

  public GraphBuilder<T> useNodeIdRenderer(NodeRenderer<? super T> nodeIdRenderer) {
    this.nodeIdRenderer = nodeIdRenderer;
    return this;
  }

  public GraphBuilder<T> useNodeNameRenderer(NodeRenderer<? super T> nodeNameRenderer) {
    this.nodeNameRenderer = nodeNameRenderer;
    return this;
  }

  public GraphBuilder<T> useEdgeRenderer(EdgeRenderer<? super T> edgeRenderer) {
    this.edgeRenderer = edgeRenderer;
    return this;
  }

  public GraphBuilder<T> omitSelfReferences() {
    this.omitSelfReferences = true;
    return this;
  }

  public GraphBuilder<T> graphFormatter(GraphFormatter formatter) {
    this.graphFormatter = formatter;
    return this;
  }

  public boolean isEmpty() {
    return this.nodeDefinitions.isEmpty();
  }

  /**
   * Adds a single node to the graph.
   *
   * @param node The node to add.
   * @return This builder.
   */
  public GraphBuilder<T> addNode(T node) {
    String nodeId = this.nodeIdRenderer.render(node);
    String nodeName = this.nodeNameRenderer.render(node);
    this.nodeDefinitions.put(nodeId, new Node<>(nodeId, nodeName, node));

    return this;
  }

  /**
   * Adds the two given nodes to the graph and creates an edge between them <strong>if they are not {@code null}</strong>.
   * Nothing will be added to the graph if one or both nodes are {@code null}.
   *
   * @param from From node.
   * @param to To node.
   * @return This builder.
   */
  // no edge will be created in case one or both nodes are null.
  public GraphBuilder<T> addEdge(T from, T to) {
    if (from != null && to != null) {
      addNode(from);
      addNode(to);

      safelyAddEdge(from, to);
    }

    return this;
  }

  /**
   * Returns the node that was added <strong>first</strong> to this builder or the given node if new.
   *
   * @param node Node.
   * @return The firstly added node or the given node if not present.
   */
  public T getEffectiveNode(T node) {
    String key = this.nodeIdRenderer.render(node);
    if (this.nodeDefinitions.containsKey(key)) {
      return this.nodeDefinitions.get(key).nodeObject;
    }

    return node;
  }

  @Override
  public String toString() {
    // Work around some generics restrictions
    ImmutableList.Builder<Node<?>> nodeListBuilder = ImmutableList.builder();
    for (Node<?> node : this.nodeDefinitions.values()) {
      nodeListBuilder.add(node);
    }
    ImmutableList<Node<?>> nodeList = nodeListBuilder.build();
    ImmutableSet<Edge> edgeSet = ImmutableSet.copyOf(this.edges);

    return this.graphFormatter.format(this.graphName, nodeList, edgeSet);
  }

  private void safelyAddEdge(T fromNode, T toNode) {
    String fromNodeId = this.nodeIdRenderer.render(fromNode);
    String toNodeId = this.nodeIdRenderer.render(toNode);

    if (!this.omitSelfReferences || !fromNodeId.equals(toNodeId)) {
      Edge edge = new Edge(fromNodeId, toNodeId, this.edgeRenderer.render(fromNode, toNode));
      this.edges.add(edge);
    }
  }

  static <T> EdgeRenderer<T> createDefaultEdgeRenderer() {
    return new EdgeRenderer<T>() {

      @Override
      public String render(T from, T to) {
        return "";
      }

    };
  }

  static <T> NodeRenderer<T> createDefaultNodeIdRenderer() {
    return new NodeRenderer<T>() {

      @Override
      public String render(T node) {
        return node.toString();
      }
    };
  }

  static <T> NodeRenderer<T> createDefaultNodeNameRenderer() {
    return new NodeRenderer<T>() {

      @Override
      public String render(T node) {
        return "";
      }
    };
  }

}
