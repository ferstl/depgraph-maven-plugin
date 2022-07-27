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
package com.github.ferstl.depgraph.dependency;

import java.util.EnumSet;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.DependencyResolutionException;
import org.apache.maven.project.DependencyResolutionRequest;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.eclipse.aether.RepositorySystemSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link MavenGraphAdapter}.
 */
class MavenGraphAdapterTest {

  private ProjectDependenciesResolver dependenciesResolver;
  private MavenProject mavenProject;
  private GraphBuilder<DependencyNode> graphBuilder;
  private ArtifactFilter globalFilter;
  private MavenGraphAdapter graphAdapter;


  @BeforeEach
  void before() throws Exception {
    Artifact projectArtifact = mock(Artifact.class);

    this.mavenProject = new MavenProject();
    this.mavenProject.setArtifact(projectArtifact);
    ProjectBuildingRequest projectBuildingRequest = mock(ProjectBuildingRequest.class);
    when(projectBuildingRequest.getRepositorySession()).thenReturn(mock(RepositorySystemSession.class));
    //noinspection deprecation
    this.mavenProject.setProjectBuildingRequest(projectBuildingRequest);

    this.globalFilter = mock(ArtifactFilter.class);
    ArtifactFilter transitiveIncludeExcludeFilter = mock(ArtifactFilter.class);
    ArtifactFilter targetFilter = mock(ArtifactFilter.class);
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);

    this.dependenciesResolver = mock(ProjectDependenciesResolver.class);
    DependencyResolutionResult dependencyResolutionResult = mock(DependencyResolutionResult.class);
    when(dependencyResolutionResult.getDependencyGraph()).thenReturn(mock(org.eclipse.aether.graph.DependencyNode.class));
    when(this.dependenciesResolver.resolve(any(DependencyResolutionRequest.class))).thenReturn(dependencyResolutionResult);

    this.graphAdapter = new MavenGraphAdapter(this.dependenciesResolver, transitiveIncludeExcludeFilter, targetFilter, EnumSet.of(INCLUDED));
  }

  @Test
  void dependencyGraph() throws Exception {
    this.graphAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);

    verify(this.dependenciesResolver).resolve(any(DependencyResolutionRequest.class));
  }

  @Test
  void dependencyGraphWithException() throws Exception {
    DependencyResolutionException exception = new DependencyResolutionException(mock(DependencyResolutionResult.class), "boom", new Exception());
    when(this.dependenciesResolver.resolve(any(DependencyResolutionRequest.class))).thenThrow(exception);

    try {
      this.graphAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);
      fail("Expect exception");
    } catch (DependencyGraphException e) {
      assertEquals(exception, e.getCause());
    }
  }
}
