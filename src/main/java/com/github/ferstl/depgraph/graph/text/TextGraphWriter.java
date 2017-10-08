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
  private final Map<String, List<Edge>> relations;
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
    for (String root : this.roots) {
      Node<?> fromNode = this.nodesById.get(root);
      stringBuilder.append(fromNode.getNodeName()).append("\n");
      writeChildren(stringBuilder, root, -1);
    }
  }

  private void writeChildren(StringBuilder stringBuilder, String parent, int level) {
    List<Edge> edges = this.relations.get(parent);
    for (int i = 0; i < edges.size(); i++) {
      Edge edge = edges.get(i);
      if (i != edges.size() - 1) {
        indent(stringBuilder, level + 1);
      } else {
        indentEnd(stringBuilder, level + 1);
      }

      Node<?> childNode = this.nodesById.get(edge.getToNodeId());
      stringBuilder.append(childNode.getNodeName());

      if (edge.getName() != null && !edge.getName().isEmpty()) {
        stringBuilder.append(" (").append(edge.getName()).append(")");
      }

      stringBuilder.append("\n");
      writeChildren(stringBuilder, childNode.getNodeId(), level + 1);
    }

    edges.clear();
  }

  private void indent(StringBuilder stringBuilder, int level) {
    for (int i = 0; i < level; i++) {
      stringBuilder.append("|  ");
    }
    stringBuilder.append("+- ");
  }

  private void indentEnd(StringBuilder stringBuilder, int level) {
    for (int i = 0; i < level; i++) {
      stringBuilder.append("|  ");
    }
    stringBuilder.append("\\- ");
  }
}
