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

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class JsonDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
  private final AtomicInteger nextId = new AtomicInteger(0);
  private final Map<Artifact, Integer> artifactToIdMap;

  public JsonDependencyNodeNameRenderer(Map<Artifact, Integer> artifactToIdMap) {
    this.artifactToIdMap = artifactToIdMap;
  }

  @Override
  public String render(DependencyNode node) {
    if (!this.artifactToIdMap.containsKey(node.getArtifact())) {
      this.artifactToIdMap.put(node.getArtifact(), this.nextId.getAndIncrement());
    }
    Integer nodeId = this.artifactToIdMap.get(node.getArtifact());
    return NEWLINE_JOINER.join(
        "{ \"id\": " + nodeId,
        "    , \"artifactId\": \"" + node.getArtifact().getArtifactId() + "\"",
        "    , \"groupId\": \"" + node.getArtifact().getGroupId() + "\"",
        "    , \"version\": \"" + node.getArtifact().getVersion() + "\"",
        "    }");
  }
}
