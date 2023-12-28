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
package com.github.ferstl.depgraph.dependency.dot;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;

import static com.github.ferstl.depgraph.dependency.VersionAbbreviator.abbreviateVersion;


public class DotDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean renderVersions;
  private final StyleConfiguration styleConfiguration;

  public DotDependencyEdgeRenderer(boolean renderVersions, StyleConfiguration styleConfiguration) {
    this.renderVersions = renderVersions;
    this.styleConfiguration = styleConfiguration;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution fromResolution = from.getResolution();
    NodeResolution toResolution = to.getResolution();

    DotAttributeBuilder builder = this.styleConfiguration.edgeAttributes(fromResolution, toResolution, to.getEffectiveScope(), from.getArtifact(), to.getArtifact());
    if (toResolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersions) {
      builder.label(abbreviateVersion(to.getArtifact().getVersion()));
    }

    return builder.toString();
  }

}
