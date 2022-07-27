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
package com.github.ferstl.depgraph.dependency.json;

import java.io.IOException;
import java.io.StringWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

public class JsonDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private final boolean renderVersions;
  private final ObjectMapper objectMapper;

  public JsonDependencyEdgeRenderer(boolean renderVersions) {
    this.renderVersions = renderVersions;
    this.objectMapper = new ObjectMapper()
        .setSerializationInclusion(NON_EMPTY)
        .setVisibility(FIELD, ANY);
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();
    boolean showVersion = resolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersions;

    DependencyData dependencyData = new DependencyData(showVersion ? to.getArtifact().getVersion() : null, resolution);

    StringWriter jsonStringWriter = new StringWriter();
    try {
      this.objectMapper.writer().writeValue(jsonStringWriter, dependencyData);
    } catch (IOException e) {
      // should never happen with StringWriter
      throw new IllegalStateException(e);
    }

    return jsonStringWriter.toString();
  }

  private static class DependencyData {

    private final String version;
    private final NodeResolution resolution;

    DependencyData(String version, NodeResolution resolution) {
      this.version = version;
      this.resolution = resolution;
    }
  }
}
