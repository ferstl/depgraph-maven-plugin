/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.gml;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

import static com.github.ferstl.depgraph.dependency.VersionAbbreviator.abbreviateVersion;

public class GmlDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean renderVersion;

  public GmlDependencyEdgeRenderer(boolean renderVersion) {
    this.renderVersion = renderVersion;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();
    StringBuilder builder = new StringBuilder();

    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT) {
      String version = "";
      if (this.renderVersion) {
        version = abbreviateVersion(to.getArtifact().getVersion());
      }

      addStyleAttributes(builder, version, "dashed", "#FF0000");

    } else if (resolution == NodeResolution.OMITTED_FOR_DUPLICATE) {
      addStyleAttributes(builder, "", "dotted", "");
    }

    return builder.toString();
  }

  private static void addStyleAttributes(StringBuilder builder, String label, String edgeStyle, String color) {
    if (!label.isEmpty()) {
      builder.append("label \"").append(label).append("\"");
    }

    builder.append("\n")
        .append("graphics\n")
        .append("[\n")
        .append("style \"").append(edgeStyle).append("\"").append("\n")
        .append("targetArrow \"").append("standard").append("\"");

    if (!color.isEmpty()) {
      builder.append("\n")
          .append("fill \"").append(color).append("\"").append("\n")
          .append("]").append("\n")
          .append("LabelGraphics").append("\n")
          .append("[").append("\n")
          .append("color \"").append(color).append("\"");
    }

    builder.append("\n")
        .append("]");
  }
}
