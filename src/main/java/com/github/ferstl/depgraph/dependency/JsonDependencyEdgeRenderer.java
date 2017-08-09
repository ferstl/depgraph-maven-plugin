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
package com.github.ferstl.depgraph.dependency;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import org.apache.maven.artifact.Artifact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import com.google.common.base.Joiner;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

public class JsonDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
  private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
  private final boolean renderVersions;
  /**
   * @deprecated ID mapping should be done in the formatter.
   */
  @Deprecated
  private final Map<Artifact, Integer> artifactToIdMap;
  private final ObjectMapper objectMapper;

  public JsonDependencyEdgeRenderer(boolean renderVersions, Map<Artifact, Integer> artifactToIdMap) {
    this.renderVersions = renderVersions;
    this.objectMapper = new ObjectMapper()
        .setSerializationInclusion(NON_EMPTY);
    this.artifactToIdMap = artifactToIdMap;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    NodeResolution resolution = to.getResolution();

    ObjectNode jsonNode = this.objectMapper.createObjectNode();
    if (resolution == NodeResolution.OMITTED_FOR_CONFLICT && this.renderVersions) {
      jsonNode.put("version", to.getArtifact().getVersion());
    }

    StringWriter jsonStringWriter = new StringWriter();
    try {
      this.objectMapper.writer().writeValue(jsonStringWriter, jsonNode);
    } catch (IOException e) {
      // should never happen with StringWriter
      throw new IllegalStateException(e);
    }

    return jsonStringWriter.toString();
  }

}
