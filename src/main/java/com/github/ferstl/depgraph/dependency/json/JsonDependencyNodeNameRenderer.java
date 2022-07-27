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
import java.util.Collection;
import org.apache.maven.artifact.Artifact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;

public class JsonDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showTypes;
  private final boolean showClassifiers;
  private final boolean showVersion;
  private final boolean showOptional;
  private final boolean showScope;
  private final ObjectMapper objectMapper;

  public JsonDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional, boolean showScope) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showTypes = showTypes;
    this.showClassifiers = showClassifiers;
    this.showVersion = showVersion;
    this.showOptional = showOptional;
    this.showScope = showScope;

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
        this.showOptional ? artifact.isOptional() : null,
        this.showClassifiers ? node.getClassifiers() : emptyList(),
        this.showScope ? (!node.getScopes().isEmpty() ? node.getScopes() : singletonList(SCOPE_COMPILE)) : emptyList(),
        this.showTypes ? node.getTypes() : emptyList());

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
    private final Boolean optional;
    private final Collection<String> classifiers;
    private final Collection<String> scopes;
    private final Collection<String> types;

    ArtifactData(
        String groupId,
        String artifactId,
        String version,
        Boolean optional,
        Collection<String> classifiers,
        Collection<String> scopes,
        Collection<String> types) {
      this.optional = optional;
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
      this.classifiers = classifiers;
      this.scopes = scopes;
      this.types = types;
    }
  }
}
