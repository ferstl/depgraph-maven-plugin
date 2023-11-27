/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.puml;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import static com.github.ferstl.depgraph.dependency.VersionAbbreviator.abbreviateVersion;

/**
 * Renders an arc between two nodes in a PlantUML diagram. Arcs are styled and colored depending on the resolution of the target node.
 */
public class PumlDependencyEgdeRenderer implements EdgeRenderer<DependencyNode> {

  private static final String INCLUDE_COLOR = "#000000"; // black
  private static final String DUPLICATE_COLOR = "#ABABAB"; // medium grey
  private static final String CONFLICT_COLOR = "#FF0000"; // red

  private final boolean renderVersion;

  public PumlDependencyEgdeRenderer(boolean renderVersion) {
    this.renderVersion = renderVersion;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();

    PumlEdgeInfo edgeInfo = new PumlEdgeInfo();

    switch (resolution) {
      case INCLUDED:
      case PARENT:
        //includeColor(edgeInfo);
        new IncludedEdgeRenderingStrategy().applyColor(edgeInfo, to);
        break;
      case OMITTED_FOR_CONFLICT:
        //conflictColor(edgeInfo, to);
        new OmittedForConflictEdgeRenderingStrategy().applyColor(edgeInfo, to);
        break;
      case OMITTED_FOR_DUPLICATE:
        //duplicateColor(edgeInfo);
        new OmittedForDuplicateEdgeRenderingStrategy().applyColor(edgeInfo, to);
        break;
      default:
        // do not output an edge in other cases
    }

    return edgeInfo.toString();
  }

  public class IncludedEdgeRenderingStrategy {
    public void applyColor(PumlEdgeInfo edgeInfo, DependencyNode to) {
      edgeInfo.withBegin("-[")
        .withColor(INCLUDE_COLOR)
        .withEnd("]->")
        .withLabel("")
        .toString();
    }
  }

  public class OmittedForConflictEdgeRenderingStrategy {
    public void applyColor(PumlEdgeInfo edgeInfo, DependencyNode to) {
      edgeInfo.withBegin(".[")
        .withColor(CONFLICT_COLOR)
        .withEnd("].>")
        .withLabel(abbreviateVersion(to.getArtifact().getVersion()))
        .toString();
    }
  }

  public class OmittedForDuplicateEdgeRenderingStrategy {
    public void applyColor(PumlEdgeInfo edgeInfo, DependencyNode to) {
      edgeInfo.withBegin(".[")
        .withColor(DUPLICATE_COLOR)
        .withEnd("].>")
        .withLabel("")
        .toString();
    }
  }
}

