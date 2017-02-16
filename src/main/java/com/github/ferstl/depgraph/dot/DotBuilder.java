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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import static com.github.ferstl.depgraph.dot.DotEscaper.escape;

/**
 * A builder to create <a href="http://www.graphviz.org/doc/info/lang.html">DOT</a> strings by defining edges between
 * Nodes. The builder allows some customizations including custom {@link NodeRenderer}s and
 * {@link EdgeRenderer}s.
 *
 * @param <T> Type of the graph nodes.
 */
public final class DotBuilder<T> {

  private String graphName;
  private AttributeBuilder nodeAttributeBuilder;
  private AttributeBuilder edgeAttributeBuilder;
  private NodeRenderer<? super T> nodeIdRenderer;
  private NodeRenderer<? super T> nodeNameRenderer;
  private EdgeRenderer<? super T> edgeRenderer;
  private boolean omitSelfReferences;
  private final Map<String, T> nodeDefinitions;
  private final Set<Edge> edges;

  public DotBuilder() {
    this.graphName = "G";
    this.nodeAttributeBuilder = new AttributeBuilder().shape("box").fontName("Helvetica");
    this.edgeAttributeBuilder = new AttributeBuilder().fontName("Helvetica").fontSize(10);
    this.nodeIdRenderer = createDefaultNodeIdRenderer();
    this.nodeNameRenderer = createDefaultNodeNameRenderer();
    this.edgeRenderer = createDefaultEdgeRenderer();

    this.nodeDefinitions = new LinkedHashMap<>();
    this.edges = new LinkedHashSet<>();
  }

  public DotBuilder<T> graphName(String name) {
    this.graphName = name;
    return this;
  }

  public DotBuilder<T> nodeStyle(AttributeBuilder attributeBuilder) {
    this.nodeAttributeBuilder = attributeBuilder;
    return this;
  }

  public DotBuilder<T> edgeStyle(AttributeBuilder attributeBuilder) {
    this.edgeAttributeBuilder = attributeBuilder;
    return this;
  }

  public DotBuilder<T> useNodeIdRenderer(NodeRenderer<? super T> nodeIdRenderer) {
    this.nodeIdRenderer = nodeIdRenderer;
    return this;
  }

  public DotBuilder<T> useNodeNameRenderer(NodeRenderer<? super T> nodeNameRenderer) {
    this.nodeNameRenderer = nodeNameRenderer;
    return this;
  }

  public DotBuilder<T> useEdgeRenderer(EdgeRenderer<? super T> edgeRenderer) {
    this.edgeRenderer = edgeRenderer;
    return this;
  }

  public DotBuilder<T> omitSelfReferences() {
    this.omitSelfReferences = true;
    return this;
  }

  // no edge will be created in case one or both nodes are null.
  public DotBuilder<T> addEdge(T from, T to) {
    if (from != null && to != null) {
      addNode(from);
      addNode(to);

      safelyAddEdge(from, to);
    }

    return this;
  }

  public DotBuilder<T> addEdge(T from, T to, EdgeRenderer<? super T> edgeRenderer) {
    EdgeRenderer<? super T> originalEdgeRenderer = this.edgeRenderer;
    this.edgeRenderer = edgeRenderer;
    addEdge(from, to);
    this.edgeRenderer = originalEdgeRenderer;

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
      return this.nodeDefinitions.get(key);
    }

    return node;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph ").append(escape(this.graphName)).append(" {")
        .append("\n  node ").append(this.nodeAttributeBuilder)
        .append("\n  edge ").append(this.edgeAttributeBuilder);

    sb.append("\n\n  // Node Definitions:");
    for (Entry<String, T> entry : this.nodeDefinitions.entrySet()) {
      String nodeId = entry.getKey();
      String nodeName = this.nodeNameRenderer.render(entry.getValue());
      sb.append("\n  ")
          .append(escape(nodeId))
          .append(nodeName);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (Edge edge : this.edges) {
      String edgeDefinition = escape(edge.fromNodeId) + " -> " + escape(edge.toNodeId) + edge.name;
      sb.append("\n  ").append(edgeDefinition);
    }

    return sb.append("\n}").toString();
  }

  private void addNode(T node) {
    String nodeId = this.nodeIdRenderer.render(node);
    this.nodeDefinitions.put(nodeId, node);
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

  static class Edge<T> {

    private final String fromNodeId;
    private final String toNodeId;
    private final String name;

    Edge(String fromNodeId, String toNodeId, String name) {
      this.fromNodeId = fromNodeId;
      this.toNodeId = toNodeId;
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) { return true; }
      if (!(o instanceof Edge)) { return false; }

      Edge<?> edge = (Edge<?>) o;
      return Objects.equals(this.fromNodeId, edge.fromNodeId)
          && Objects.equals(this.toNodeId, edge.toNodeId)
          && Objects.equals(this.name, edge.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.fromNodeId, this.toNodeId, this.name);
    }
  }
}
