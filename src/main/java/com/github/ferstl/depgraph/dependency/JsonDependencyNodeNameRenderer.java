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
import java.util.Collection;
import java.util.Collections;
import org.apache.maven.artifact.Artifact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferstl.depgraph.graph.NodeRenderer;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Collections.singleton;

public class JsonDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;
  private final ObjectMapper objectMapper;

  public JsonDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;

    this.objectMapper = new ObjectMapper()
        .setSerializationInclusion(NON_EMPTY)
        .setVisibility(FIELD, ANY);
  }

  @Override
  public String render(DependencyNode node) {
    Artifact artifact = node.getArtifact();
    ArtifactData artifactData = new ArtifactData(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null,
        !isNullOrEmpty(artifact.getClassifier()) ? singleton(artifact.getClassifier()) : Collections.<String>emptyList(),
        node.getScopes(),
        singleton(artifact.getType()));

    StringWriter jsonStringWriter = new StringWriter();
    try {
      this.objectMapper.writer().writeValue(jsonStringWriter, artifactData);
    } catch (IOException e) {
      // should never happen with StringWriter
      throw new IllegalStateException(e);
    }

    return jsonStringWriter.toString();
  }


  private static class ArtifactData {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Collection<String> classifiers;
    private final Collection<String> scopes;
    private final Collection<String> types;

    ArtifactData(
        String groupId,
        String artifactId,
        String version,
        Collection<String> classifiers,
        Collection<String> scopes,
        Collection<String> types) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
      this.classifiers = classifiers;
      this.scopes = scopes;
      this.types = types;
    }
  }
}
