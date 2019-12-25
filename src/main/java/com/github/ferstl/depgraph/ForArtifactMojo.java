/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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
public class ForArtifactMojo extends DependencyGraphMojo {

  /**
   * The {@code gropId} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "groupId", required = true)
  private String groupId;

  /**
   * The {@code artifactId} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "artifactId", required = true)
  private String artifactId;

  /**
   * The {@code version} of the artifact.
   *
   * @since 3.3.0
   */
  @Parameter(property = "version", required = true)
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

    DefaultArtifact artifact = new DefaultArtifact(this.groupId, this.artifactId, this.version, SCOPE_COMPILE, this.type, this.classifier, new DefaultArtifactHandler());
    try {
      return this.projectBuilder.build(artifact, buildingRequest).getProject();
    } catch (ProjectBuildingException e) {
      throw new IllegalStateException("Error while creating Maven project from Artifact '" + artifact + "'.", e);
    }

  }
}
