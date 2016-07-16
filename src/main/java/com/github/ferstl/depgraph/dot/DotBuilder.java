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
import java.util.Set;
import static com.github.ferstl.depgraph.dot.DotEscaper.escape;

/**
 * A builder to create <a href="http://www.graphviz.org/doc/info/lang.html">DOT</a> strings by defining edges between
 * Nodes. The builder allows some customizations including custom {@link NodeNameRenderer}s and
 * {@link EdgeAttributeRenderer}s.
 *
 * @param <T> Type of the graph nodes.
 */
public final class DotBuilder<T> {

  private String graphName;
  private AttributeBuilder nodeAttributeBuilder;
  private AttributeBuilder edgeAttributeBuilder;
  private NodeNameRenderer<? super T> nodeNameRenderer;
  private NodeAttributeRenderer<? super T> nodeAttributeRenderer;
  private EdgeAttributeRenderer<? super T> edgeAttributeRenderer;
  private boolean omitSelfReferences;
  private final Map<String, T> nodeDefinitions;
  private final Set<String> edgeDefinitions;

  public DotBuilder() {
    this.graphName = "G";
    this.nodeAttributeBuilder = new AttributeBuilder().shape("box").fontName("Helvetica");
    this.edgeAttributeBuilder = new AttributeBuilder().fontName("Helvetica").fontSize(10);
    this.nodeNameRenderer = createDefaultNodeNameRenderer();
    this.nodeAttributeRenderer = createDefaultNodeAttributeRenderer();
    this.edgeAttributeRenderer = createDefaultEdgeAttributeRenderer();

    this.nodeDefinitions = new LinkedHashMap<>();
    this.edgeDefinitions = new LinkedHashSet<>();
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

  public DotBuilder<T> useNodeNameRenderer(NodeNameRenderer<? super T> nodeNameRenderer) {
    this.nodeNameRenderer = nodeNameRenderer;
    return this;
  }

  public DotBuilder<T> useNodeAttributeRenderer(NodeAttributeRenderer<? super T> nodeAttributeRenderer) {
    this.nodeAttributeRenderer = nodeAttributeRenderer;
    return this;
  }

  public DotBuilder<T> useEdgeAttributeRenderer(EdgeAttributeRenderer<? super T> edgeAttributeRenderer) {
    this.edgeAttributeRenderer = edgeAttributeRenderer;
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

  public DotBuilder<T> addEdge(T from, T to, EdgeAttributeRenderer<? super T> edgeAttributeRenderer) {
    EdgeAttributeRenderer<? super T> originalEdgeAttributeRenderer = this.edgeAttributeRenderer;
    this.edgeAttributeRenderer = edgeAttributeRenderer;
    addEdge(from, to);
    this.edgeAttributeRenderer = originalEdgeAttributeRenderer;

    return this;
  }

  /**
   * Returns the node that was added <strong>first</strong> to this builder or the given node if new.
   *
   * @param node Node.
   * @return The firstly added node or the given node if not present.
   */
  public T getEffectiveNode(T node) {
    String key = escape(this.nodeNameRenderer.createNodeName(node));
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
      AttributeBuilder nodeAttributes = this.nodeAttributeRenderer.createNodeAttributes(entry.getValue());
      sb.append("\n  ")
          .append(entry.getKey())
          .append(nodeAttributes.toString());
    }

    sb.append("\n\n  // Edge Definitions:");
    for (String edge : this.edgeDefinitions) {
      sb.append("\n  ").append(edge);
    }

    return sb.append("\n}").toString();
  }

  private void addNode(T node) {
    String nodeName = this.nodeNameRenderer.createNodeName(node);
    this.nodeDefinitions.put(escape(nodeName), node);
  }

  private void safelyAddEdge(T fromNode, T toNode) {
    String fromName = this.nodeNameRenderer.createNodeName(fromNode);
    String toName = this.nodeNameRenderer.createNodeName(toNode);

    if (!this.omitSelfReferences || !fromName.equals(toName)) {
      String edgeDefinition = escape(fromName) + " -> " + escape(toName) + this.edgeAttributeRenderer.createEdgeAttributes(fromNode, toNode);
      this.edgeDefinitions.add(edgeDefinition);
    }
  }

  static <T> EdgeAttributeRenderer<T> createDefaultEdgeAttributeRenderer() {
    return new EdgeAttributeRenderer<T>() {

      @Override
      public AttributeBuilder createEdgeAttributes(T from, T to) {
        return new AttributeBuilder();
      }

    };
  }

  static <T> NodeNameRenderer<T> createDefaultNodeNameRenderer() {
    return new NodeNameRenderer<T>() {

      @Override
      public String createNodeName(T node) {
        return node.toString();
      }
    };
  }

  static <T> NodeAttributeRenderer<T> createDefaultNodeAttributeRenderer() {
    return new NodeAttributeRenderer<T>() {

      @Override
      public AttributeBuilder createNodeAttributes(T node) {
        return new AttributeBuilder();
      }
    };
  }
}
