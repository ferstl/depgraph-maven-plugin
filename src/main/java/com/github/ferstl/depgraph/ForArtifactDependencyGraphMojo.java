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
package com.github.ferstl.depgraph;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;

/**
 * Creates a dependency graph from an arbitrary artifact.
 *
 * @since 3.3.0
 */
@Mojo(
    name = "for-artifact",
    defaultPhase = LifecyclePhase.NONE,
    requiresProject = false,
    requiresDirectInvocation = true,
    requiresDependencyCollection = ResolutionScope.NONE,
    requiresDependencyResolution = ResolutionScope.NONE,
    threadSafe = true)
public class ForArtifactDependencyGraphMojo extends DependencyGraphMojo {

  /**
   * Artifact in the form of {@code groupId:artifactId:version[:packaging[:classifier]]}.
   * This is a shorter alternative for the {@code groupId}, {@code artifactId}, {@code version},
   * {@code type}, {@code classifier} options.
   *
   * @since 4.0.0
   */
  @Parameter(property = "artifact")
  private String artifact;

  /**
   * The {@code gropId} of the artifact. Required if {@code artifact} is not defined.
   *
   * @since 3.3.0
   */
  @Parameter(property = "groupId")
  private String groupId;

  /**
   * The {@code artifactId} of the artifact. Required if {@code artifact} is not defined.
   *
   * @since 3.3.0
   */
  @Parameter(property = "artifactId")
  private String artifactId;

  /**
   * The {@code version} of the artifact. Required if {@code artifact} is not defined.
   *
   * @since 3.3.0
   */
  @Parameter(property = "version")
  private String version;

  /**
   * The {@code type} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "type", defaultValue = "jar")
  private String type;

  /**
   * The {@code classifier} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "classifier", defaultValue = "")
  private String classifier;

  /**
   * The {@code artifactId} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "profiles")
  private List<String> profiles;

  @Parameter(defaultValue = "${session}", readonly = true, required = true)
  private MavenSession session;

  @Component
  private ProjectBuilder projectBuilder;

  @Override
  public MavenProject getProject() {
    ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(this.session.getProjectBuildingRequest());
    buildingRequest.setRepositorySession(this.session.getRepositorySession());
    buildingRequest.setProject(null);
    buildingRequest.setResolveDependencies(true);
    buildingRequest.setActiveProfileIds(this.profiles);

    Artifact artifact = createArtifact();
    try {
      return this.projectBuilder.build(artifact, buildingRequest).getProject();
    } catch (ProjectBuildingException e) {
      throw new IllegalStateException("Error while creating Maven project from Artifact '" + artifact + "'.", e);
    }

  }

  private Artifact createArtifact() {
    validateParameters();

    String groupId = this.groupId;
    String artifactId = this.artifactId;
    String version = this.version;
    String type = this.type;
    String classifier = this.classifier;

    if (StringUtils.isNotBlank(this.artifact)) {
      String[] parts = this.artifact.split(":");

      // At least groupId/artifactId/version is required
      if (parts.length < 3) {
        throw new IllegalArgumentException("Invalid artifact definition: " + this.artifact);
      }

      groupId = parts[0];
      artifactId = parts[1];
      version = parts[2];
      if (parts.length > 3 && StringUtils.isNotBlank(parts[3])) {
        type = parts[3];
      }
      if (parts.length > 4) {
        classifier = parts[4];
      }

    }

    return new DefaultArtifact(groupId, artifactId, version, SCOPE_COMPILE, type, classifier, new DefaultArtifactHandler());
  }

  private void validateParameters() {
    // Either artifact or GAV parameters
    if (checkNotBlank()) {
      // GAV parameters must not be set
      isNotBlank();
      // All GAV parameters have to be set
    } else if (checkBlank()) {
      throw new IllegalArgumentException("'groupId', 'artifactId' and 'version' parameters have to be defined");
    }
  }

  private boolean checkNotBlank(){
    if(StringUtils.isNotBlank(this.artifact)) return true;
    return false;
  }

  private boolean checkBlank(){
    if (Stream.of(this.groupId, this.artifactId, this.version).anyMatch(StringUtils::isBlank)) {
      return true;
    }
    return false;
  }

  private void isNotBlank(){
    if (Stream.of(this.groupId, this.artifactId, this.version).anyMatch(StringUtils::isNotBlank)) {
      throw new IllegalArgumentException("Artifact can be defined with either 'artifact' or 'groupId'/'artifactId'/'version' but not both");
    }
  }
}
