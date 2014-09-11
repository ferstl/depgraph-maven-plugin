package com.github.ferstl.depgraph;

import java.util.Arrays;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.ferstl.depgraph.dot.DotBuilder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for {@link AggregatingGraphFactory}.
 */
public class AggregatingGraphFactoryTest {

  private ArtifactFilter artifactFilter;
  private DependencyGraphBuilder graphBuilder;
  private GraphBuilderAdapter adapter;
  private DotBuilder dotBuilder;

  @Before
  public void before() throws Exception {
    this.artifactFilter = mock(ArtifactFilter.class);
    when(this.artifactFilter.include(Mockito.<Artifact>any())).thenReturn(true);

    DependencyNode dependencyNode = mock(DependencyNode.class);
    this.graphBuilder = mock(DependencyGraphBuilder.class);
    when(this.graphBuilder.buildDependencyGraph(Mockito.<MavenProject>any(), Mockito.<ArtifactFilter>any())).thenReturn(dependencyNode);

    this.adapter = new GraphBuilderAdapter(this.graphBuilder);

    this.dotBuilder = new DotBuilder();
  }

  @Test
  public void test() throws Exception {
    AggregatingGraphFactory graphFactory = new AggregatingGraphFactory(this.adapter, this.artifactFilter, this.dotBuilder, true);

    MavenProject parent = createMavenProject("parent");
    MavenProject child1 = createMavenProject("child1", parent);
    MavenProject child2 = createMavenProject("child2", parent);
    parent.setCollectedProjects(Arrays.asList(child1, child2));

    graphFactory.createGraph(parent);

    Mockito.verify(this.graphBuilder).buildDependencyGraph(parent, this.artifactFilter);
  }


  private MavenProject createMavenProject(String artifactId) {
    MavenProject project = new MavenProject();
    DefaultArtifact artifact = new DefaultArtifact("groupId", artifactId, "version", "compile", "jar", "", null);
    project.setArtifact(artifact);
    return project;
  }

  private MavenProject createMavenProject(String artifactId, MavenProject parent) {
    MavenProject project = createMavenProject(artifactId);
    project.setParent(parent);

    return project;
  }

  // nested

  //stop at parent

  // Modules in graph -> yes, no


  // parent not part of graph

  // filtered project

}
