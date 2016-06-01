/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import static com.github.ferstl.depgraph.dot.DotEscaper.escape;

/**
 * A builder to create <a href="http://www.graphviz.org/doc/info/lang.html">DOT</a> strings by defining edges between
 * {@link Node}s. The builder allows some customizations including custom {@link NodeRenderer}s and
 * {@link EdgeRenderer}s.
 */
public final class DotBuilder {

  private NodeRenderer nodeRenderer;
  private NodeRenderer nodeLabelRenderer;
  private EdgeRenderer edgeRenderer;
  private boolean omitSelfReferences;
  private final Map<Node, NodeDefinition> nodeDefinitions;
  private final Set<EdgeDefinition> edgeDefinitions;

  public DotBuilder() {
    this.nodeLabelRenderer = DefaultRenderer.INSTANCE;
    this.nodeRenderer = DefaultRenderer.INSTANCE;
    this.edgeRenderer = DefaultRenderer.INSTANCE;

    this.nodeDefinitions = new LinkedHashMap<>();
    this.edgeDefinitions = new LinkedHashSet<>();
  }

  public DotBuilder useNodeRenderer(NodeRenderer nodeRenderer) {
    this.nodeRenderer = nodeRenderer;
    return this;
  }

  public DotBuilder useNodeLabelRenderer(NodeRenderer nodeLabelRenderer) {
    this.nodeLabelRenderer = nodeLabelRenderer;
    return this;
  }

  public DotBuilder useEdgeRenderer(EdgeRenderer edgeRenderer) {
    this.edgeRenderer = edgeRenderer;
    return this;
  }

  public DotBuilder omitSelfReferences() {
    this.omitSelfReferences = true;
    return this;
  }

  // no edge will be created in case one or both nodes are null.
  public DotBuilder addEdge(Node from, Node to) {
    if (from != null && to != null) {
      addNode(from);
      addNode(to);

      safelyAddEdge(from, to);
    }

    return this;
  }

  public Node getNode(Node key) {
    NodeDefinition nodeDefinition = this.nodeDefinitions.get(key);
    return nodeDefinition != null ? nodeDefinition.node : null;
  }

  public DotBuilder addEdge(Node from, Node to, EdgeRenderer edgeRenderer) {
    EdgeRenderer originalEdgeRenderer = this.edgeRenderer;
    this.edgeRenderer = edgeRenderer;
    addEdge(from, to);
    this.edgeRenderer = originalEdgeRenderer;

    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("digraph G {")
        .append("\n  node ").append(new AttributeBuilder().shape("box").fontName("Helvetica"))
        .append("\n  edge ").append(new AttributeBuilder().fontName("Helvetica").fontSize(10));

    sb.append("\n\n  // Node Definitions:");
    for (String nodeDefinition : uniqueToString(this.nodeDefinitions.values())) {
      sb.append("\n  ").append(nodeDefinition);
    }

    sb.append("\n\n  // Edge Definitions:");
    for (String edgeDefinition : uniqueToString(this.edgeDefinitions)) {
      sb.append("\n  ").append(edgeDefinition);
    }

    return sb.append("\n}").toString();
  }

  private void addNode(Node node) {
    // If a node definition does already exist, use the node of the existing definition
    NodeDefinition nodeDefinition = this.nodeDefinitions.get(node);
    Node effectiveNode = nodeDefinition != null ? nodeDefinition.node : node;

    NodeDefinition newNodeDefinition = new NodeDefinition(effectiveNode, this.nodeRenderer, this.nodeLabelRenderer);
    this.nodeDefinitions.put(effectiveNode, newNodeDefinition);
  }

  private void safelyAddEdge(Node fromNode, Node toNode) {
    if (!this.omitSelfReferences || !fromNode.equals(toNode)) {
      this.edgeDefinitions.add(new EdgeDefinition(this.nodeRenderer, this.edgeRenderer, fromNode, toNode));
    }
  }

  /**
   * Transforms the elements of the given collection into its {@code toString()} representation and returns a unique
   * collection of them. This is needed to get unique node and edge strings since rendering may produce duplicates.
   * This happens for example when aggregating maven modules which don't have a classifier.
   */
  private Collection<String> uniqueToString(Collection<?> collection) {
    return ImmutableSet.copyOf(Collections2.transform(collection, Functions.toStringFunction()));
  }


  enum DefaultRenderer implements EdgeRenderer, NodeRenderer {
    INSTANCE;

    @Override
    public String createEdgeAttributes(Node from, Node to) {
      return "";
    }

    @Override
    public String render(Node node) {
      return node.getArtifact().toString();
    }

  }

  static final class NodeDefinition {

    final Node node;
    final NodeRenderer nodeRenderer;
    final NodeRenderer nodeLabelRenderer;

    NodeDefinition(Node node, NodeRenderer nodeRenderer, NodeRenderer nodeLabelRenderer) {
      this.node = node;
      this.nodeRenderer = nodeRenderer;
      this.nodeLabelRenderer = nodeLabelRenderer;
    }

    @Override
    public String toString() {
      String nodeName = this.nodeRenderer.render(this.node);
      String nodeLabel = this.nodeLabelRenderer.render(this.node);

      return escape(nodeName) + new AttributeBuilder().label(nodeLabel);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof NodeDefinition)) {
        return false;
      }

      NodeDefinition other = (NodeDefinition) obj;

      return Objects.equals(this.node, other.node);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.node);
    }
  }

  static final class EdgeDefinition {

    NodeRenderer nodeRenderer;
    EdgeRenderer edgeRenderer;
    Node from;
    Node to;

    EdgeDefinition(NodeRenderer nodeRenderer, EdgeRenderer edgeRenderer, Node from, Node to) {
      this.nodeRenderer = nodeRenderer;
      this.edgeRenderer = edgeRenderer;
      this.from = from;
      this.to = to;
    }

    @Override
    public String toString() {
      String fromName = this.nodeRenderer.render(this.from);
      String toName = this.nodeRenderer.render(this.to);
      return escape(fromName) + " -> " + escape(toName) + this.edgeRenderer.createEdgeAttributes(this.from, this.to);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof EdgeDefinition)) {
        return false;
      }

      EdgeDefinition other = (EdgeDefinition) obj;

      return Objects.equals(this.from, other.from)
          && Objects.equals(this.to, other.to);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.from, this.to);
    }
  }
}
