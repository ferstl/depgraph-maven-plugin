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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import org.mockito.Mockito;

import com.github.ferstl.depgraph.dot.DotBuilder;

/**
 * JUnit tests for {@link GraphBuilderAdapter}.
 */
public class GraphBuilderAdapterTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private DependencyGraphBuilder dependencyGraphBuilder;
  private DependencyTreeBuilder dependencyTreeBuilder;
  private MavenProject mavenProject;
  private DotBuilder dotBuilder;
  private ArtifactFilter artifactFilter;
  private List<String> targetDependencies;
  private ArtifactRepository artifactRepository;

  private GraphBuilderAdapter graphAdapter;
  private GraphBuilderAdapter treeAdapter;


  @Before
  public void before() throws Exception {
    this.mavenProject = new MavenProject();
    this.artifactFilter = mock(ArtifactFilter.class);
    this.dotBuilder = new DotBuilder();

    this.dependencyGraphBuilder = mock(DependencyGraphBuilder.class);
    when(this.dependencyGraphBuilder.buildDependencyGraph(Mockito.<MavenProject>any(), Mockito.<ArtifactFilter>any())).thenReturn(mock(org.apache.maven.shared.dependency.graph.DependencyNode.class));

    this.dependencyTreeBuilder = mock(DependencyTreeBuilder.class);
    when(this.dependencyTreeBuilder.buildDependencyTree(Mockito.<MavenProject>any(), Mockito.<ArtifactRepository>any(), Mockito.<ArtifactFilter>any())).thenReturn(mock(org.apache.maven.shared.dependency.tree.DependencyNode.class));

    this.targetDependencies = new ArrayList<>();
    
    this.artifactRepository = mock(ArtifactRepository.class);
    this.graphAdapter = new GraphBuilderAdapter(this.dependencyGraphBuilder, this.targetDependencies);
    this.treeAdapter = new GraphBuilderAdapter(this.dependencyTreeBuilder, this.artifactRepository, this.targetDependencies);
  }

  @Test
  public void dependencyGraph() throws Exception {
    this.graphAdapter.buildDependencyGraph(this.mavenProject, this.artifactFilter, this.dotBuilder);

    verify(this.dependencyGraphBuilder).buildDependencyGraph(this.mavenProject, this.artifactFilter);
    verify(this.dependencyTreeBuilder, never()).buildDependencyTree(Mockito.<MavenProject>any(), Mockito.<ArtifactRepository>any(), Mockito.<ArtifactFilter>any());
  }

  @Test
  public void dependencyGraphWithException() throws Exception {
    DependencyGraphBuilderException cause = new DependencyGraphBuilderException("boom");
    when(this.dependencyGraphBuilder.buildDependencyGraph(Mockito.<MavenProject>any(), Mockito.<ArtifactFilter>any())).thenThrow(cause);

    this.expectedException.expect(DependencyGraphException.class);
    this.expectedException.expectCause(is(cause));

    this.graphAdapter.buildDependencyGraph(this.mavenProject, this.artifactFilter, this.dotBuilder);
  }

  @Test
  public void dependencyTree() throws Exception {
    this.treeAdapter.buildDependencyGraph(this.mavenProject, this.artifactFilter, this.dotBuilder);
    verify(this.dependencyTreeBuilder).buildDependencyTree(this.mavenProject, this.artifactRepository, this.artifactFilter);
    verify(this.dependencyGraphBuilder, never()).buildDependencyGraph(Mockito.<MavenProject>any(), Mockito.<ArtifactFilter>any());
  }


  @Test
  public void dependencyTreeWithException() throws Exception {
    DependencyTreeBuilderException cause = new DependencyTreeBuilderException("boom");
    when(this.dependencyTreeBuilder.buildDependencyTree(Mockito.<MavenProject>any(), Mockito.<ArtifactRepository>any(), Mockito.<ArtifactFilter>any())).thenThrow(cause);

    this.expectedException.expect(DependencyGraphException.class);
    this.expectedException.expectCause(is(cause));

    this.treeAdapter.buildDependencyGraph(this.mavenProject, this.artifactFilter, this.dotBuilder);
  }
}
