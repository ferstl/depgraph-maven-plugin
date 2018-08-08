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
import java.util.Collection;
import java.util.EnumSet;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.DependencyResolutionRequest;
import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.eclipse.aether.RepositorySystemSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;
import static com.github.ferstl.depgraph.graph.GraphBuilderMatcher.emptyGraph;
import static com.github.ferstl.depgraph.graph.GraphBuilderMatcher.hasNodesAndEdges;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link AggregatingGraphFactory}.
 * <p>
 * The {@link AggregatingGraphFactory} is a bit tricky to test since it cannot use a real instance of
 * {@link ProjectDependenciesResolver} because it would require other components of the Maven ecosystem. Instead, we use a
 * mock of the {@link ProjectDependenciesResolver} here. So to verify the correct behavior of
 * {@link AggregatingGraphFactory}, we can only check for the expected invocations of {@link ProjectDependenciesResolver}
 * instead of verifying the created graph. However, the parent/child relations are not a result of
 * {@link ProjectDependenciesResolver} invocations since they are directly created via the {@link GraphBuilder}. So these
 * relations need to be tested by verifying the {@link GraphBuilder}.
 * </p>
 */
public class AggregatingGraphFactoryTest {

  private ArtifactFilter globalFilter;
  private ProjectDependenciesResolver dependenciesResolver;
  private MavenGraphAdapter adapter;
  private GraphBuilder<DependencyNode> graphBuilder;
  private SubProjectSupplier collectedProjectSupplier;


  @Before
  public void before() throws Exception {
    this.globalFilter = mock(ArtifactFilter.class);
    ArtifactFilter transitiveIncludeExcludeFilter = mock(ArtifactFilter.class);
    ArtifactFilter targetFilter = mock(ArtifactFilter.class);
    when(this.globalFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);
    when(transitiveIncludeExcludeFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);
    when(targetFilter.include(ArgumentMatchers.<Artifact>any())).thenReturn(true);

    DependencyResolutionResult dependencyResolutionResult = mock(DependencyResolutionResult.class);
    org.eclipse.aether.graph.DependencyNode dependencyNode = mock(org.eclipse.aether.graph.DependencyNode.class);
    when(dependencyResolutionResult.getDependencyGraph()).thenReturn(dependencyNode);

    this.dependenciesResolver = mock(ProjectDependenciesResolver.class);
    when(this.dependenciesResolver.resolve(any(DependencyResolutionRequest.class))).thenReturn(dependencyResolutionResult);

    this.adapter = new MavenGraphAdapter(this.dependenciesResolver, transitiveIncludeExcludeFilter, targetFilter, EnumSet.of(INCLUDED));
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);
    this.collectedProjectSupplier = new SubProjectSupplier() {

      @Override
      public Collection<MavenProject> getSubProjects(MavenProject parent) {
        return parent.getCollectedProjects();
      }
    };
  }

  /**
   * T.
   * <pre>
   * parent
   *   - child1
   *   - child2
   * include parents
   * </pre>
   */
  @Test
  public void moduleTree() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, true, false);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1", parent);
    createMavenProject("child2", parent);

    graphFactory.createGraph(parent);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"",
            "\"groupId:child1:jar:version:compile\"",
            "\"groupId:child2:jar:version:compile\""},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * <pre>
   * parent
   * - child1
   * - child2
   * exclude parents
   * </pre>
   */
  @Test
  public void excludeParentProjects() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, false, false);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1", parent);
    createMavenProject("child2", parent);

    graphFactory.createGraph(parent);

    verify(this.dependenciesResolver, never()).resolve(argThat(projectName("parent")));
    verify(this.dependenciesResolver).resolve(argThat(projectName("child1")));
    verify(this.dependenciesResolver).resolve(argThat(projectName("child2")));

    // There are no module nodes. So because of the mocked graph builder the graph must be empty here.
    assertThat(this.graphBuilder, emptyGraph());
  }


  /**
   * .
   * <pre>
   * parent
   * - child1-1
   * - child1-2
   * - subParent
   *   - child2-1
   *   - child2-2
   * include parents
   * </pre>
   */
  @Test
  public void nestedProjects() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, true, true);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1-1", parent);
    createMavenProject("child1-2", parent);
    MavenProject subParent = createMavenProject("subParent", parent);
    createMavenProject("child2-1", subParent);
    createMavenProject("child2-2", subParent);

    graphFactory.createGraph(parent);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"",
            "\"groupId:child1-1:jar:version:compile\"",
            "\"groupId:child1-2:jar:version:compile\"",
            "\"groupId:subParent:jar:version:compile\"",
            "\"groupId:child2-1:jar:version:compile\"",
            "\"groupId:child2-2:jar:version:compile\""},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-2:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:subParent:jar:version:compile\"[style=dotted]",
            "\"groupId:subParent:jar:version:compile\" -> \"groupId:child2-1:jar:version:compile\"[style=dotted]",
            "\"groupId:subParent:jar:version:compile\" -> \"groupId:child2-2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * <pre>
   * parentParent (not part of graph)
   * - parent
   *   - child
   * </pre>
   */
  @Test
  public void stopAtParent() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, true, false);

    MavenProject parentParent = createMavenProject("parentParent");
    MavenProject parent = createMavenProject("parent", parentParent);
    createMavenProject("child", parent);

    graphFactory.createGraph(parent);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"",
            "\"groupId:child:jar:version:compile\""},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * <pre>
   * parent
   * - child1-1
   * - child1-2
   * - subParent
   *   - child2-1
   *   - child2-2
   * subParent is excluded
   * </pre>
   */
  @Test
  public void filteredParent() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, true, false);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1-1", parent);
    createMavenProject("child1-2", parent);
    MavenProject subParent = createMavenProject("subParent", parent);
    createMavenProject("child2-1", subParent);
    createMavenProject("child2-2", subParent);

    when(this.globalFilter.include(subParent.getArtifact())).thenReturn(false);

    graphFactory.createGraph(parent);

    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"",
            "\"groupId:child1-1:jar:version:compile\"",
            "\"groupId:child1-2:jar:version:compile\""},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * <pre>
   * parent
   * - child1
   * - child2
   * child1 is excluded
   * </pre>
   */
  @Test
  public void excludedArtifact() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.collectedProjectSupplier, this.globalFilter, this.graphBuilder, true, false);

    MavenProject parent = createMavenProject("parent");
    MavenProject child1 = createMavenProject("child1", parent);
    createMavenProject("child2", parent);

    when(this.globalFilter.include(child1.getArtifact())).thenReturn(false);

    graphFactory.createGraph(parent);

    // graph builder must not be invoked for child1
    verify(this.dependenciesResolver, never()).resolve(argThat(projectName("child1")));


    // module tree must not contain child1
    assertThat(this.graphBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"",
            "\"groupId:child2:jar:version:compile\""},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\"[style=dotted]"}));
  }


  private MavenProject createMavenProject(String artifactId) {
    MavenProject project = new MavenProject();
    project.setArtifactId(artifactId);
    // Make sure that we can modify the list later.
    project.setCollectedProjects(new ArrayList<MavenProject>());

    DefaultArtifact artifact = new DefaultArtifact("groupId", artifactId, "version", "compile", "jar", "", null);
    project.setArtifact(artifact);

    RepositorySystemSession repositorySession = mock(RepositorySystemSession.class);
    ProjectBuildingRequest projectBuildingRequest = mock(ProjectBuildingRequest.class);
    when(projectBuildingRequest.getRepositorySession()).thenReturn(repositorySession);
    //noinspection deprecation
    project.setProjectBuildingRequest(projectBuildingRequest);

    return project;
  }

  private MavenProject createMavenProject(String artifactId, MavenProject parent) {
    MavenProject project = createMavenProject(artifactId);
    project.setParent(parent);
    parent.getModules().add(artifactId);

    MavenProject currentParent = parent;
    while (currentParent != null) {
      currentParent.getCollectedProjects().add(project);
      currentParent = currentParent.getParent();
    }

    return project;
  }

  private static ArgumentMatcher<DependencyResolutionRequest> projectName(String projectName) {
    return new ProjectNameMatcher(projectName);
  }

  private static class ProjectNameMatcher implements ArgumentMatcher<DependencyResolutionRequest> {

    private final String expectedProjectName;

    public ProjectNameMatcher(String projectName) {
      this.expectedProjectName = projectName;
    }

    @Override
    public boolean matches(DependencyResolutionRequest argument) {
      return argument.getMavenProject().getName().equals(this.expectedProjectName);
    }
  }
}
