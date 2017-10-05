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

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import com.github.ferstl.depgraph.ToStringNodeIdRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link MavenGraphAdapter}.
 */
public class MavenGraphAdapterTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private DependencyGraphBuilder dependencyGraphBuilder;
  private DependencyTreeBuilder dependencyTreeBuilder;
  private MavenProject mavenProject;
  private GraphBuilder<DependencyNode> graphBuilder;
  private ArtifactFilter globalFilter;
  private ArtifactFilter targetFilter;
  private ArtifactRepository artifactRepository;

  private MavenGraphAdapter graphAdapter;
  private MavenGraphAdapter treeAdapter;


  @Before
  public void before() throws Exception {
    this.mavenProject = new MavenProject();
    this.globalFilter = mock(ArtifactFilter.class);
    this.targetFilter = mock(ArtifactFilter.class);
    this.graphBuilder = GraphBuilder.create(ToStringNodeIdRenderer.INSTANCE);

    this.dependencyGraphBuilder = mock(DependencyGraphBuilder.class);
    when(this.dependencyGraphBuilder.buildDependencyGraph(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactFilter>any())).thenReturn(mock(org.apache.maven.shared.dependency.graph.DependencyNode.class));

    this.dependencyTreeBuilder = mock(DependencyTreeBuilder.class);
    when(this.dependencyTreeBuilder.buildDependencyTree(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactRepository>any(), ArgumentMatchers.<ArtifactFilter>any())).thenReturn(mock(org.apache.maven.shared.dependency.tree.DependencyNode.class));


    this.artifactRepository = mock(ArtifactRepository.class);
    this.graphAdapter = new MavenGraphAdapter(this.dependencyGraphBuilder, this.targetFilter, false);
    this.treeAdapter = new MavenGraphAdapter(this.dependencyTreeBuilder, this.artifactRepository, this.targetFilter, allOf(NodeResolution.class));
  }

  @Test
  public void dependencyGraph() throws Exception {
    this.graphAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);

    verify(this.dependencyGraphBuilder).buildDependencyGraph(this.mavenProject, this.globalFilter);
    verify(this.dependencyTreeBuilder, never()).buildDependencyTree(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactRepository>any(), ArgumentMatchers.<ArtifactFilter>any());
  }

  @Test
  public void dependencyGraphWithException() throws Exception {
    DependencyGraphBuilderException cause = new DependencyGraphBuilderException("boom");
    when(this.dependencyGraphBuilder.buildDependencyGraph(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactFilter>any())).thenThrow(cause);

    this.expectedException.expect(DependencyGraphException.class);
    this.expectedException.expectCause(is(cause));

    this.graphAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);
  }

  @Test
  public void dependencyTree() throws Exception {
    this.treeAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);
    verify(this.dependencyTreeBuilder).buildDependencyTree(this.mavenProject, this.artifactRepository, this.globalFilter);
    verify(this.dependencyGraphBuilder, never()).buildDependencyGraph(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactFilter>any());
  }


  @Test
  public void dependencyTreeWithException() throws Exception {
    DependencyTreeBuilderException cause = new DependencyTreeBuilderException("boom");
    when(this.dependencyTreeBuilder.buildDependencyTree(ArgumentMatchers.<MavenProject>any(), ArgumentMatchers.<ArtifactRepository>any(), ArgumentMatchers.<ArtifactFilter>any())).thenThrow(cause);

    this.expectedException.expect(DependencyGraphException.class);
    this.expectedException.expectCause(is(cause));

    this.treeAdapter.buildDependencyGraph(this.mavenProject, this.globalFilter, this.graphBuilder);
  }
}
