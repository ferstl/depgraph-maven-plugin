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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import static com.github.ferstl.depgraph.dot.DotEscaper.escape;

/**
 * A builder to create <a href="http://www.graphviz.org/doc/info/lang.html">DOT</a> strings by defining edges between
 * Nodes. The builder allows some customizations including custom {@link NodeRenderer}s and {@link EdgeRenderer}s.
 *
 * @param <T> Type of the graph nodes.
 */
public final class DotBuilder<T> {

  private NodeRenderer<? super T> nodeRenderer;
  private NodeRenderer<? super T> nodeLabelRenderer;
  private EdgeRenderer<? super T> edgeRenderer;
  private boolean omitSelfReferences;
  private final Set<String> nodeDefinitions;
  private final Set<String> edgeDefinitions;
  private final Map<String, T> nodeMap;

  public DotBuilder() {
    this.nodeLabelRenderer = createDefaultNodeRenderer();
    this.nodeRenderer = createDefaultNodeRenderer();
    this.edgeRenderer = createDefaultEdgeRenderer();

    this.nodeDefinitions = new LinkedHashSet<>();
    this.edgeDefinitions = new LinkedHashSet<>();
    this.nodeMap = new HashMap<>();
  }

  public DotBuilder<T> useNodeRenderer(NodeRenderer<? super T> nodeRenderer) {
    this.nodeRenderer = nodeRenderer;
    return this;
  }

  public DotBuilder<T> useNodeLabelRenderer(NodeRenderer<? super T> nodeLabelRenderer) {
    this.nodeLabelRenderer = nodeLabelRenderer;
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
    String key = this.nodeRenderer.render(node);
    if (this.nodeMap.containsKey(key)) {
      return this.nodeMap.get(key);
    }

    return node;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {")
        .append("\n  node ").append(new AttributeBuilder().shape("box").fontName("Helvetica"))
        .append("\n  edge ").append(new AttributeBuilder().fontName("Helvetica").fontSize(10));

    sb.append("\n\n  // Node Definitions:");
    for (String node : this.nodeDefinitions) {
      sb.append("\n  ").append(node);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (String edge : this.edgeDefinitions) {
      sb.append("\n  ").append(edge);
    }

    return sb.append("\n}").toString();
  }

  private void addNode(T node) {
    String nodeName = this.nodeRenderer.render(node);
    String nodeLabel = this.nodeLabelRenderer.render(node);

    String nodeDefinition = escape(nodeName) + new AttributeBuilder().label(nodeLabel);
    this.nodeDefinitions.add(nodeDefinition);

    if (!this.nodeMap.containsKey(nodeName)) {
      this.nodeMap.put(nodeName, node);
    }
  }

  private void safelyAddEdge(T fromNode, T toNode) {
    String fromName = this.nodeRenderer.render(fromNode);
    String toName = this.nodeRenderer.render(toNode);

    if (!this.omitSelfReferences || !fromName.equals(toName)) {
      String edgeDefinition = escape(fromName) + " -> " + escape(toName) + this.edgeRenderer.createEdgeAttributes(fromNode, toNode);
      this.edgeDefinitions.add(edgeDefinition);
    }
  }

  static <T> EdgeRenderer<T> createDefaultEdgeRenderer() {
    return new EdgeRenderer<T>() {

      @Override
      public String createEdgeAttributes(T from, T to) {
        return "";
      }

    };
  }

  static <T> NodeRenderer<T> createDefaultNodeRenderer() {
    return new NodeRenderer<T>() {

      @Override
      public String render(T node) {
        return node.toString();
      }
    };
  }
}
