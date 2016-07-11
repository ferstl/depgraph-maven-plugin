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
import com.github.ferstl.depgraph.dot.EdgeAttributeRenderer;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import static com.google.common.collect.Iterables.getFirst;


public class DependencyEdgeAttributeRenderer implements EdgeAttributeRenderer<GraphNode> {

  private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

  private final boolean renderVersions;
  private final StyleConfiguration styleConfiguration;

  public DependencyEdgeAttributeRenderer(boolean renderVersions, StyleConfiguration styleConfiguration) {
    this.renderVersions = renderVersions;
    this.styleConfiguration = styleConfiguration;
  }

  @Override
  public AttributeBuilder createEdgeAttributes(GraphNode from, GraphNode to) {
    NodeResolution resolution = to.getResolution();

    AttributeBuilder builder = this.styleConfiguration.edgeAttributes(resolution, getFirst(to.getScopes(), null));
    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersions) {
      builder.label(abbreviateVersion(to.getArtifact().getVersion()));
    }

    return builder;
  }

  private String abbreviateVersion(String version) {
    if (version.endsWith(SNAPSHOT_SUFFIX)) {
      return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length()) + "-S.";
    }

    return version;
  }
}
