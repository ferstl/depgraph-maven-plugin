package com.github.ferstl.depgraph.graph.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

public class TextGraphFormatter implements com.github.ferstl.depgraph.graph.GraphFormatter {

  private final boolean repeatTransitiveDependencies;

  public TextGraphFormatter(boolean repeatTransitiveDependencies) {
    this.repeatTransitiveDependencies = repeatTransitiveDependencies;
  }

  @Override
  public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
    TextGraphWriter writer = new TextGraphWriter(nodes, edges, this.repeatTransitiveDependencies);
    StringBuilder graphStringBuilder = new StringBuilder();
    writer.write(graphStringBuilder);

    return graphStringBuilder.toString();
  }

  private static class TextGraphWriter {

    private static final String INDENTATION_FOR_PARENT = "|  ";
    private static final String INDENTATION_FOR_LAST_PARENT = "   ";
    private static final String ELEMENT_MARKER = "+- ";
    private static final String LAST_ELEMENT_MARKER = "\\- ";

    private final Map<String, Node<?>> nodesById;
    private final Map<String, Collection<Edge>> relations;
    private final Collection<String> roots;
    private final boolean repeatTransitiveDependencies;

    TextGraphWriter(Collection<Node<?>> nodes, Collection<Edge> edges, boolean repeatTransitiveDependencies) {
      this.repeatTransitiveDependencies = repeatTransitiveDependencies;
      this.nodesById = new LinkedHashMap<>();
      this.relations = new LinkedHashMap<>();
      this.roots = new LinkedHashSet<>();

      initializeGraphData(nodes);
      initializeRootElements(edges);
    }

    void write(StringBuilder stringBuilder) {
      Iterator<String> rootIterator = this.roots.iterator();
      while (rootIterator.hasNext()) {
        String root = rootIterator.next();
        Node<?> fromNode = this.nodesById.get(root);
        stringBuilder.append(fromNode.getNodeName()).append("\n");

        List<Boolean> lastParents = new ArrayList<>();
        lastParents.add(!rootIterator.hasNext());
        writeChildren(stringBuilder, root, lastParents);
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

    private void writeChildren(StringBuilder stringBuilder, String parent, List<Boolean> lastParents) {
      Collection<Edge> edges = this.relations.get(parent);
      Iterator<Edge> edgeIterator = edges.iterator();

      while (edgeIterator.hasNext()) {
        Edge edge = edgeIterator.next();
        Node<?> childNode = this.nodesById.get(edge.getToNodeId());

        // Write the current child node
        indent(stringBuilder, lastParents, !edgeIterator.hasNext());
        writeChildNode(stringBuilder, childNode.getNodeName(), edge.getName());

        // Recursively write sub tree
        lastParents.add(!edgeIterator.hasNext());
        writeChildren(stringBuilder, childNode.getNodeId(), lastParents);
        lastParents.remove(lastParents.size() - 1);
      }

      if (!this.repeatTransitiveDependencies) {
        edges.clear();
      }
    }

    private void indent(StringBuilder stringBuilder, List<Boolean> lastParents, boolean lastElement) {
      // Don't indent after the root element
      for (int i = 1; i < lastParents.size(); i++) {
        stringBuilder.append(lastParents.get(i) ? INDENTATION_FOR_LAST_PARENT : INDENTATION_FOR_PARENT);
      }

      // Use different element markers depending on whether the element is the last one in the sub tree.
      if (lastElement) {
        stringBuilder.append(LAST_ELEMENT_MARKER);
      } else {
        stringBuilder.append(ELEMENT_MARKER);
      }
    }

    private void writeChildNode(StringBuilder stringBuilder, String childNodeName, String edgeName) {
      stringBuilder.append(childNodeName);
      if (edgeName != null && !edgeName.isEmpty()) {
        stringBuilder.append(" (").append(edgeName).append(")");
      }
      stringBuilder.append("\n");
    }
  }
}
