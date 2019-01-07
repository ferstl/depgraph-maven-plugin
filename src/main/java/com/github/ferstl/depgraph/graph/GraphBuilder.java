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

import java.util.HashSet;
import java.util.Iterator;
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

  private final NodeRenderer<? super T> nodeIdRenderer;
  private final Map<String, Node<T>> nodeDefinitions;
  private final Set<Edge> edges;
  private final ReachabilityMap reachabilityMap;

  private String graphName;
  private GraphFormatter graphFormatter;
  private NodeRenderer<? super T> nodeNameRenderer;
  private EdgeRenderer<? super T> edgeRenderer;
  private boolean omitSelfReferences;

  public static <T> GraphBuilder<T> create(NodeRenderer<? super T> nodeIdRenderer) {
    return new GraphBuilder<>(nodeIdRenderer);
  }

  private GraphBuilder(NodeRenderer<? super T> nodeIdRenderer) {
    this.nodeIdRenderer = nodeIdRenderer;
    this.nodeDefinitions = new LinkedHashMap<>();
    this.edges = new LinkedHashSet<>();
    this.reachabilityMap = new ReachabilityMap();

    DotAttributeBuilder graphAttributeBuilder = new DotAttributeBuilder();
    DotAttributeBuilder nodeAttributeBuilder = new DotAttributeBuilder().shape("box").fontName("Helvetica");
    DotAttributeBuilder edgeAttributeBuilder = new DotAttributeBuilder().fontName("Helvetica").fontSize(10);

    this.graphName = "G";
    this.graphFormatter = new DotGraphFormatter(graphAttributeBuilder, nodeAttributeBuilder, edgeAttributeBuilder);
    this.nodeNameRenderer = createDefaultNodeNameRenderer();
    this.edgeRenderer = createDefaultEdgeRenderer();
  }

  public GraphBuilder<T> graphName(String name) {
    this.graphName = name;
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

  public GraphBuilder<T> addEdge(T from, T to) {
    return addEdgeInternal(from, to, false);
  }

  public GraphBuilder<T> addPermanentEdge(T from, T to) {
    return addEdgeInternal(from, to, true);
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

  public void reduceEdges() {
    Iterator<Edge> edgeIterator = this.edges.iterator();
    while (edgeIterator.hasNext()) {
      Edge edge = edgeIterator.next();
      if (!edge.isPermanent() && this.reachabilityMap.hasOlderPath(edge.getToNodeId(), edge.getFromNodeId())) {
        edgeIterator.remove();
      }
    }
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

  /**
   * Adds the two given nodes to the graph and creates an edge between them <strong>if they are not {@code null}</strong>.
   * Nothing will be added to the graph if one or both nodes are {@code null}.
   *
   * @param from From node.
   * @param to To node.
   * @param permanent Whether the edge is permanent.
   * @return This builder.
   */
  private GraphBuilder<T> addEdgeInternal(T from, T to, boolean permanent) {
    if (from != null && to != null) {
      addNode(from);
      addNode(to);

      safelyAddEdge(from, to, permanent);
    }

    return this;
  }

  private void safelyAddEdge(T fromNode, T toNode, boolean permanent) {
    String fromNodeId = this.nodeIdRenderer.render(fromNode);
    String toNodeId = this.nodeIdRenderer.render(toNode);

    if (!this.omitSelfReferences || !fromNodeId.equals(toNodeId)) {
      Edge edge = new Edge(fromNodeId, toNodeId, this.edgeRenderer.render(fromNode, toNode), permanent);
      this.edges.add(edge);
      this.reachabilityMap.registerEdge(fromNodeId, toNodeId);
    }
  }

  private static <T> EdgeRenderer<T> createDefaultEdgeRenderer() {
    return new EdgeRenderer<T>() {

      @Override
      public String render(T from, T to) {
        return "";
      }

    };
  }

  private static <T> NodeRenderer<T> createDefaultNodeNameRenderer() {
    return new NodeRenderer<T>() {

      @Override
      public String render(T node) {
        return "";
      }
    };
  }

  /**
   * A map that tracks which nodes are reachable from other nodes.
   * When a new edge 'A -> B' is added, the map registers node 'A' as a parent of node 'B'. To find out whether a node
   * 'Y' is reachable from node 'X', we can recursively traverse the parents of node 'Y'. When node 'X' is found in this
   * traversal, 'Y' is reachable via 'X'. When all nodes are traversed and 'X' is not found, 'Y' is not reachable via 'X'.
   * To handle cycles in the graph, the node traversal keeps track of all already traversed nodes.
   */
  private static class ReachabilityMap {

    private final Map<String, Set<String>> parentIndex = new LinkedHashMap<>();

    void registerEdge(String from, String to) {
      Set<String> parents = safelyGetParents(to);
      parents.add(from);
    }

    boolean hasOlderPath(String target, String source) {
      return isReachable(target, source, true, new HashSet<String>());
    }


    /**
     * Recursively traverses the parents of {@code target} trying to find {@code source} by keeping track of already traversed
     * nodes. If {@code olderParentsOnly} is set to {@code true}, only the parents that were inserted <strong>before</strong>
     * {@code source} will be considered.
     *
     * @return {@code true} if {@code target} is reachable via {@code source}, {@code false} else.
     */
    private boolean isReachable(String target, String source, boolean olderParentsOnly, Set<String> alreadyVisited) {
      if (alreadyVisited.contains(target)) {
        return false;
      }

      alreadyVisited.add(target);

      Set<String> parents = olderParentsOnly ? getOlderParents(target, source) : safelyGetParents(target);
      if (parents.contains(source)) {
        return true;
      }

      for (String parent : parents) {
        if (isReachable(parent, source, false, alreadyVisited)) {
          return true;
        }
      }

      return false;
    }

    private Set<String> getOlderParents(String target, String source) {
      Set<String> olderParents = new LinkedHashSet<>(safelyGetParents(target));
      boolean remove = false;
      Iterator<String> iterator = olderParents.iterator();
      while (iterator.hasNext()) {
        String value = iterator.next();
        if (value.equals(source)) {
          remove = true;
        }

        if (remove) {
          iterator.remove();
        }
      }

      return olderParents;
    }

    private Set<String> safelyGetParents(String node) {
      Set<String> parentPath = this.parentIndex.get(node);
      if (parentPath == null) {
        parentPath = new LinkedHashSet<>();
        this.parentIndex.put(node, parentPath);
      }

      return parentPath;
    }

  }

}
