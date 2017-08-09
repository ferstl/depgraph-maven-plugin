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
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.maven.artifact.Artifact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

public class JsonDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
  private final AtomicInteger nextId = new AtomicInteger(0);
  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;
  private final ObjectMapper objectMapper;
  /**
   * @deprecated ID mapping should be done in the formatter
   */
  @Deprecated
  private final Map<Artifact, Integer> artifactToIdMap;

  public JsonDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion, Map<Artifact, Integer> artifactToIdMap) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
    this.artifactToIdMap = artifactToIdMap;

    this.objectMapper = new ObjectMapper()
        .setSerializationInclusion(NON_EMPTY);
  }

  @Override
  public String render(DependencyNode node) {
    if (!this.artifactToIdMap.containsKey(node.getArtifact())) {
      this.artifactToIdMap.put(node.getArtifact(), this.nextId.getAndIncrement());
    }
    Integer nodeId = this.artifactToIdMap.get(node.getArtifact());

    StringWriter jsonStringWriter = new StringWriter();
    ObjectNode jsonNode = this.objectMapper.createObjectNode()
        .put("groupId", this.showGroupId ? node.getArtifact().getGroupId() : null)
        .put("artifactId", this.showArtifactId ? node.getArtifact().getArtifactId() : null)
        .put("version", this.showVersion ? node.getArtifact().getVersion() : null);

    try {
      this.objectMapper.writer().writeValue(jsonStringWriter, jsonNode);
    } catch (IOException e) {
      // should never happen with StringWriter
      throw new IllegalStateException(e);
    }

    return jsonStringWriter.toString();
  }
}
