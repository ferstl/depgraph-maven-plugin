package com.github.ferstl.depgraph.graph.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

public class TextGraphWriter {

  private final Map<String, Node<?>> nodesById;
  private final Map<String, Collection<Edge>> relations;
  private final Collection<String> roots;

  public TextGraphWriter(Collection<Node<?>> nodes, Collection<Edge> edges) {
    this.nodesById = new HashMap<>();
    this.relations = new LinkedHashMap<>();
    this.roots = new LinkedHashSet<>();

    for (Node<?> node : nodes) {
      String nodeId = node.getNodeId();
      this.nodesById.put(nodeId, node);
      this.relations.put(nodeId, new ArrayList<Edge>());
    }

    this.roots.addAll(this.nodesById.keySet());
    for (Edge edge : edges) {
      this.relations.get(edge.getFromNodeId()).add(edge);

      if (!edge.getFromNodeId().equals(edge.getToNodeId())) {
        this.roots.remove(edge.getToNodeId());
      }
    }
  }

  public void write(StringBuilder stringBuilder) {
    writeInternal(stringBuilder, this.roots, 0, true);
  }

  private void writeInternal(StringBuilder stringBuilder, Collection<String> nodeIds, int level, boolean writeParent) {

    for (String nodeId : nodeIds) {

      if (writeParent) {
        Node<?> fromNode = this.nodesById.get(nodeId);
        indent(stringBuilder, level);
        stringBuilder.append(fromNode.getNodeName()).append("\n");
      }

      Collection<Edge> edges = this.relations.get(nodeId);
      for (Edge edge : edges) {
        indent(stringBuilder, level + 1);
        Node<?> toNode = this.nodesById.get(edge.getToNodeId());
        stringBuilder.append(toNode.getNodeName());

        if (edge.getName() != null && !edge.getName().isEmpty()) {
          stringBuilder.append(" (").append(edge.getName()).append(")");
        }

        stringBuilder.append("\n");
        writeInternal(stringBuilder, getImmediateChildren(toNode.getNodeId()), level + 2, false);
      }

      writeInternal(stringBuilder, getImmediateChildren(nodeId), level + 1, false);
    }
  }

  private void indent(StringBuilder stringBuilder, int level) {
    for (int i = 0; i < level; i++) {
      stringBuilder.append("  ");
    }
  }

  private Collection<String> getImmediateChildren(String nodeId) {
    Collection<Edge> edges = this.relations.get(nodeId);
    List<String> immediateChildren = new ArrayList<>(edges.size());

    for (Edge edge : edges) {
      immediateChildren.add(edge.getToNodeId());
    }

    return immediateChildren;
  }

}
