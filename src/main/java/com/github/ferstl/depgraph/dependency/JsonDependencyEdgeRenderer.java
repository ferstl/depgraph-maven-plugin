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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import com.google.common.base.Joiner;

public class JsonDependencyEdgeRenderer implements EdgeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();
  private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
  private final Map<Artifact, Integer> artifactToIdMap;

  public JsonDependencyEdgeRenderer(Map<Artifact, Integer> artifactToIdMap) {
    this.artifactToIdMap = artifactToIdMap;
  }

  @Override
  public String render(DependencyNode from, DependencyNode to) {
    List<String> scopeStrings = new ArrayList<>();
    for (String scope : to.getScopes()) {
      scopeStrings.add("\"" + scope + "\"");
    }

    return NEWLINE_JOINER.join(
        "{ \"from\": " + this.artifactToIdMap.get(from.getArtifact()),
        "    , \"to\": " + this.artifactToIdMap.get(to.getArtifact()),
        "    , \"resolution\": \"" + to.getResolution() + "\"",
        "    , \"scopes\": [ " + COMMA_JOINER.join(scopeStrings) + " ]",
        "    }");
  }

}
