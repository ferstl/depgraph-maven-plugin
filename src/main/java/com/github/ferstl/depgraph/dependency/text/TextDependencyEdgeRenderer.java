/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.text;

import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class TextDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean showVersions;

  public TextDependencyEdgeRenderer(boolean showVersionOnEdges) {
    this.showVersions = showVersionOnEdges;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    switch (to.getResolution()) {
      case OMITTED_FOR_CONFLICT:
        String message = "omitted for conflict";
        if (this.showVersions) {
          message += ": " + to.getArtifact().getVersion();
        }

        return message;

      case OMITTED_FOR_DUPLICATE:
        return "omitted for duplicate";

      default:
        return "";
    }
  }
}
