package com.github.ferstl.depgraph.graph.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

public class TextGraphFormatter implements com.github.ferstl.depgraph.graph.GraphFormatter {

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    TextGraphWriter writer = new TextGraphWriter(nodes, edges);
    StringBuilder graphStringBuilder = new StringBuilder();
    writer.write(graphStringBuilder);

    return graphStringBuilder.toString();
  }

  static class TextGraphWriter {

    private static final String INDENTATION_FOR_PARENT = "|  ";
    private static final String INDENTATION_FOR_LAST_PARENT = "   ";
    private static final String ELEMENT_MARKER = "+- ";
    private static final String LAST_ELEMENT_MARKER = "\\- ";

    private final Map<String, Node<?>> nodesById;
    private final Map<String, Collection<Edge>> relations;
    private final Collection<String> roots;

    private boolean alreadyUsed;

    TextGraphWriter(Collection<Node<?>> nodes, Collection<Edge> edges) {
      this.nodesById = new HashMap<>();
      this.relations = new LinkedHashMap<>();
      this.roots = new LinkedHashSet<>();
      this.alreadyUsed = false;

      initializeGraphData(nodes);
      initializeRootElements(edges);
    }

    void write(StringBuilder stringBuilder) {
      if (this.alreadyUsed) {
        throw new IllegalStateException("This writer has already been used.");
      }

      try {
        writeInternal(stringBuilder);
      } finally {
        this.alreadyUsed = true;
      }
    }

    private void writeInternal(StringBuilder stringBuilder) {
      Iterator<String> rootIterator = this.roots.iterator();
      while (rootIterator.hasNext()) {
        String root = rootIterator.next();
        Node<?> fromNode = this.nodesById.get(root);
        stringBuilder.append(fromNode.getNodeName()).append("\n");
        writeChildren(stringBuilder, root, 0, !rootIterator.hasNext());
      }
    }

    private void initializeGraphData(Collection<Node<?>> nodes) {
      for (Node<?> node : nodes) {
        String nodeId = node.getNodeId();
        this.nodesById.put(nodeId, node);
        this.relations.put(nodeId, new ArrayList<Edge>());
      }
    }

    private void initializeRootElements(Collection<Edge> edges) {
      this.roots.addAll(this.nodesById.keySet());
      for (Edge edge : edges) {
        this.relations.get(edge.getFromNodeId()).add(edge);

        if (!edge.getFromNodeId().equals(edge.getToNodeId())) {
          this.roots.remove(edge.getToNodeId());
        }
      }
    }

    private void writeChildren(StringBuilder stringBuilder, String parent, int level, boolean lastParent) {
      Collection<Edge> edges = this.relations.get(parent);
      Iterator<Edge> edgeIterator = edges.iterator();

      while (edgeIterator.hasNext()) {
        Edge edge = edgeIterator.next();
        indent(stringBuilder, level, lastParent, !edgeIterator.hasNext());

        Node<?> childNode = this.nodesById.get(edge.getToNodeId());
        stringBuilder.append(childNode.getNodeName());

        if (edge.getName() != null && !edge.getName().isEmpty()) {
          stringBuilder.append(" (").append(edge.getName()).append(")");
        }

        stringBuilder.append("\n");
        writeChildren(stringBuilder, childNode.getNodeId(), level + 1, !edgeIterator.hasNext());
      }

      // Don't repeat already written edges
      edges.clear();
    }

    private void indent(StringBuilder stringBuilder, int level, boolean lastParent, boolean lastElement) {
      // Indent up to the previous level
      for (int i = 0; i < level - 1; i++) {
        stringBuilder.append(INDENTATION_FOR_PARENT);
      }

      // Indent the last level depending on whether the parent is the last element in the tree
      if (level > 0) {
        stringBuilder.append(lastParent ? INDENTATION_FOR_LAST_PARENT : INDENTATION_FOR_PARENT);
      }

      // Use different element markers depending on whether the element is the last one in the sub tree.
      if (lastElement) {
        stringBuilder.append(LAST_ELEMENT_MARKER);
      } else {
        stringBuilder.append(ELEMENT_MARKER);
      }
    }
  }
}
