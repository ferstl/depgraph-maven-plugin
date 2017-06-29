package com.github.ferstl.depgraph.graph.puml;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.PumlEdgeInfo;
import com.github.ferstl.depgraph.dependency.PumlNodeInfo;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.GraphFormatter;
import com.github.ferstl.depgraph.graph.Node;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Graph formatter for <a href="PlantUML">http://plantuml.com/component-diagram</a> diagram.
 *
 * @author gushakov
 */
public class PumlGraphFormatter implements GraphFormatter {

    @Override
    public String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges) {
        final StringBuilder puml = new StringBuilder();

        startUml(puml);

        skinParam(puml);

        writeNodes(puml, nodes);

        writeEdges(puml, edges);

        endUml(puml);

        return puml.toString();
    }

    private void startUml(StringBuilder puml) {
        puml.append("@startuml\n");
    }

    private void skinParam(StringBuilder puml) {
        puml.append("skinparam rectangle {\n")
                .append("  BackgroundColor<<test>> lightGreen\n")
                .append("  BackgroundColor<<runtime>> lightBlue\n")
                .append("  BackgroundColor<<provided>> lightGray\n")
                .append("}\n");
    }

    private void writeNodes(StringBuilder puml, Collection<Node<?>> nodes) {
        for (final Node<?> node : nodes) {

            final PumlNodeInfo nodeInfo = PumlNodeInfo.parse(node.getNodeName());

            puml.append(nodeInfo.getComponent())
                    .append(" \"")
                    .append(nodeInfo.getLabel())
                    .append("\" as ")
                    .append(escape(node.getNodeId()));


            if (!nodeInfo.getStereotype().equals("compile")) {
                puml.append("<<")
                        .append(nodeInfo.getStereotype())
                        .append(">>");
            }


            puml.append("\n");
        }
    }

    private void writeEdges(StringBuilder puml, Collection<Edge> edges) {
        for (final Edge edge : edges) {
            final PumlEdgeInfo edgeInfo = PumlEdgeInfo.parse(edge.getName());
            puml.append(escape(edge.getFromNodeId()))
                    .append(" ")
                    .append(edgeInfo.getBegin())
                    .append(edgeInfo.getColor())
                    .append(edgeInfo.getEnd())
                    .append(" ")
                    .append(escape(edge.getToNodeId()));

            if (edgeInfo.getLabel() != null && !edgeInfo.getLabel().equals("")) {
                puml.append(": ")
                        .append(edgeInfo.getLabel());
            }

            puml.append("\n");
        }
    }

    private void endUml(StringBuilder puml) {
        puml.append("@enduml");
    }

    private String escape(String id) {
        return StringUtils.removeEnd(id.replaceAll("\\W", "_"), "_");
    }
}
