package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.EdgeRenderer;

/**
 * Renders an arc between two nodes in a PlantUML diagram. Arcs are styled and colored depending on the resolution of the target node.
 */
public class PumlDependencyEgdeRenderer implements EdgeRenderer<DependencyNode> {

  private static final String INCLUDE_COLOR = "#000000"; // black
  private static final String DUPLICATE_COLOR = "#D3D3D3"; // lightGray
  private static final String CONFLICT_COLOR = "#FF0000"; // red

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();

    PumlEdgeInfo edgeInfo = new PumlEdgeInfo();

    switch (resolution) {
      case INCLUDED:

        edgeInfo.withBegin("-[")
            .withColor(INCLUDE_COLOR)
            .withEnd("]->")
            .withLabel("");
        break;
      case OMITTED_FOR_CONFLICT:
        edgeInfo.withBegin(".[")
            .withColor(CONFLICT_COLOR)
            .withEnd("].>")
            .withLabel(to.getArtifact().getVersion());
        break;
      case OMITTED_FOR_DUPLICATE:
        edgeInfo.withBegin(".[")
            .withColor(DUPLICATE_COLOR)
            .withEnd("].>")
            .withLabel(to.getArtifact().getVersion());
        break;
      case OMITTED_FOR_CYCLE:
      default:
        // do not output an edge in other cases
    }

    return edgeInfo.toString();
  }
}
