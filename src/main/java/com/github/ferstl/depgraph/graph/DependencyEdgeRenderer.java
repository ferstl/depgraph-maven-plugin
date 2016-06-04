/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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

import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.EdgeRenderer;


public class DependencyEdgeRenderer implements EdgeRenderer<GraphNode> {

  private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

  private final boolean renderVersions;
  private final boolean renderDuplicates;
  private final boolean renderConflicts;

  public DependencyEdgeRenderer(boolean renderVersions, boolean renderDuplicates, boolean renderConflicts) {
    this.renderVersions = renderVersions;
    this.renderDuplicates = renderDuplicates;
    this.renderConflicts = renderConflicts;
  }

  @Override
  public String createEdgeAttributes(GraphNode from, GraphNode to) {
    AttributeBuilder builder = new AttributeBuilder();
    NodeResolution resolution = to.getResolution();

    if (this.renderDuplicates && resolution == NodeResolution.OMITTED_FOR_DUPLICATE) {
      builder.style("dashed");
    }

    if (this.renderConflicts && resolution == NodeResolution.OMITTED_FOR_CONFLICT) {
      builder
          .style("dashed")
          .color("red")
          .fontColor("red");

      if (this.renderVersions) {
        builder.label(abbreviateVersion(to.getArtifact().getVersion()));
      }
    }

    return builder.toString();
  }

  private String abbreviateVersion(String version) {
    if (version.endsWith(SNAPSHOT_SUFFIX)) {
      return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length()) + "-S.";
    }

    return version;
  }
}
