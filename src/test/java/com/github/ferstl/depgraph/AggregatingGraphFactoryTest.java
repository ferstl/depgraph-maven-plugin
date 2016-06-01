/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
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

import java.util.ArrayList;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import com.github.ferstl.depgraph.dot.DotBuilder;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.emptyGraph;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodesAndEdges;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link AggregatingGraphFactory}.
 * <p>
 * The {@link AggregatingGraphFactory} is a bit tricky to test since it cannot use a real instance of
 * {@link DependencyGraphBuilder} because it would require other components of the Maven ecosystem. Instead, we use a
 * mock of the {@link DependencyGraphBuilder} here. So to verify the correct behavior of
 * {@link AggregatingGraphFactory}, we can only check for the expected invocations of {@link DependencyGraphBuilder}
 * instead of verifying the created graph. However, the parent/child relations are not a result of
 * {@link DependencyGraphBuilder} invocations since they are directly created via the {@link DotBuilder}. So these
 * relations need to be tested by verifying the {@link DotBuilder}.
 * </p>
 */
public class AggregatingGraphFactoryTest {

  private ArtifactFilter globalFilter;
  private ArtifactFilter targetFilter;
  private DependencyGraphBuilder graphBuilder;
  private GraphBuilderAdapter adapter;
  private DotBuilder dotBuilder;

  @Before
  public void before() throws Exception {
    this.globalFilter = mock(ArtifactFilter.class);
    this.targetFilter = mock(ArtifactFilter.class);
    when(this.globalFilter.include(Matchers.<Artifact>any())).thenReturn(true);
    when(this.targetFilter.include(Matchers.<Artifact>any())).thenReturn(true);

    DependencyNode dependencyNode = mock(DependencyNode.class);
    this.graphBuilder = mock(DependencyGraphBuilder.class);
    when(this.graphBuilder.buildDependencyGraph(Matchers.<MavenProject>any(), Matchers.<ArtifactFilter>any())).thenReturn(dependencyNode);

    this.adapter = new GraphBuilderAdapter(this.graphBuilder, this.targetFilter);

    this.dotBuilder = new DotBuilder();
  }

  /**
   * T.
   *
   * <pre>
   * parent
   *   - child1
   *   - child2
   *
   * include parents
   * </pre>
   */
  @Test
  public void moduleTree() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, true);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1", parent);
    createMavenProject("child2", parent);

    graphFactory.createGraph(parent);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child1:jar:version:compile\"[label=\"groupId:child1:jar:version:compile\"]",
            "\"groupId:child2:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * 
   * <pre>
   * parent
   * - child1
   * - child2
   *
   * exclude parents
   * </pre>
   */
  @Test
  public void excludeParentProjects() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, false);

    MavenProject parent = createMavenProject("parent");
    MavenProject child1 = createMavenProject("child1", parent);
    MavenProject child2 = createMavenProject("child2", parent);

    graphFactory.createGraph(parent);

    verify(this.graphBuilder, never()).buildDependencyGraph(parent, this.globalFilter);
    verify(this.graphBuilder).buildDependencyGraph(child1, this.globalFilter);
    verify(this.graphBuilder).buildDependencyGraph(child2, this.globalFilter);

    // There are no module nodes. So because of the mocked graph builder the graph must be empty here.
    assertThat(this.dotBuilder, emptyGraph());
  }


  /**
   * .
   * 
   * <pre>
   * parent
   * - child1-1
   * - child1-2
   * - subParent
   *   - child2-1
   *   - child2-2
   *
   * include parents
   * </pre>
   */
  @Test
  public void nestedProjects() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, true);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1-1", parent);
    createMavenProject("child1-2", parent);
    MavenProject subParent = createMavenProject("subParent", parent);
    createMavenProject("child2-1", subParent);
    createMavenProject("child2-2", subParent);

    graphFactory.createGraph(parent);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child1-1:jar:version:compile\"[label=\"groupId:child1-1:jar:version:compile\"]",
            "\"groupId:child1-2:jar:version:compile\"[label=\"groupId:child1-2:jar:version:compile\"]",
            "\"groupId:subParent:jar:version:compile\"[label=\"groupId:subParent:jar:version:compile\"]",
            "\"groupId:child2-1:jar:version:compile\"[label=\"groupId:child2-1:jar:version:compile\"]",
            "\"groupId:child2-2:jar:version:compile\"[label=\"groupId:child2-2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-2:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:subParent:jar:version:compile\"[style=dotted]",
            "\"groupId:subParent:jar:version:compile\" -> \"groupId:child2-1:jar:version:compile\"[style=dotted]",
            "\"groupId:subParent:jar:version:compile\" -> \"groupId:child2-2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * 
   * <pre>
   * parentParent (not part of graph)
   * - parent
   *   - child
   * </pre>
   */
  @Test
  public void stopAtParent() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, true);

    MavenProject parentParent = createMavenProject("parentParent");
    MavenProject parent = createMavenProject("parent", parentParent);
    createMavenProject("child", parent);

    graphFactory.createGraph(parent);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child:jar:version:compile\"[label=\"groupId:child:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * 
   * <pre>
   * parent
   * - child1-1
   * - child1-2
   * - subParent
   *   - child2-1
   *   - child2-2
   *
   * subParent is excluded
   * </pre>
   */
  @Test
  public void filteredParent() {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, true);

    MavenProject parent = createMavenProject("parent");
    createMavenProject("child1-1", parent);
    createMavenProject("child1-2", parent);
    MavenProject subParent = createMavenProject("subParent", parent);
    createMavenProject("child2-1", subParent);
    createMavenProject("child2-2", subParent);

    when(this.globalFilter.include(subParent.getArtifact())).thenReturn(false);

    graphFactory.createGraph(parent);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child1-1:jar:version:compile\"[label=\"groupId:child1-1:jar:version:compile\"]",
            "\"groupId:child1-2:jar:version:compile\"[label=\"groupId:child1-2:jar:version:compile\"]"},
        new String[]{
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-1:jar:version:compile\"[style=dotted]",
            "\"groupId:parent:jar:version:compile\" -> \"groupId:child1-2:jar:version:compile\"[style=dotted]"}));
  }

  /**
   * .
   * 
   * <pre>
   * parent
   * - child1
   * - child2
   *
   * child1 is excluded
   * </pre>
   */
  @Test
  public void excludedArtifact() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.globalFilter, this.dotBuilder, true);

    MavenProject parent = createMavenProject("parent");
    MavenProject child1 = createMavenProject("child1", parent);
    createMavenProject("child2", parent);

    when(this.globalFilter.include(child1.getArtifact())).thenReturn(false);

    graphFactory.createGraph(parent);

    // graph builder must not be invoked for child1
    verify(this.graphBuilder, never()).buildDependencyGraph(child1, this.globalFilter);


    // module tree must not contain child1
    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[]{
            "\"groupId:parent:jar:version:compile\"[label=\"groupId:parent:jar:version:compile\"]",
            "\"groupId:child2:jar:version:compile\"[label=\"groupId:child2:jar:version:compile\"]"},
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

    return project;
  }

  @SuppressWarnings("unchecked")
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

}
