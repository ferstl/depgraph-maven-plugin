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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;
import static org.apache.maven.artifact.Artifact.SCOPE_PROVIDED;
import static org.apache.maven.artifact.Artifact.SCOPE_RUNTIME;
import static org.apache.maven.artifact.Artifact.SCOPE_TEST;
import static org.eclipse.aether.util.graph.transformer.ConflictResolver.NODE_DATA_WINNER;

/**
 * Creates an example graph. This Mojo has the same capabilities as the {@code graph} Mojo. So it might be useful to
 * try out different options and node stylings.
 *
 * @since 2.0.0
 */
@Mojo(
    name = "example",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.NONE,
    threadSafe = true)
public class ExampleDependencyGraphMojo extends DependencyGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {
    GraphBuilder<DependencyNode> graphBuilder = createGraphBuilder(graphStyleConfigurer);
    return new ExampleGraphFactory(graphBuilder, globalFilter, targetFilter);
  }


  static class ExampleGraphFactory implements GraphFactory {

    private final GraphBuilder<DependencyNode> graphBuilder;
    private final ArtifactFilter globalFilter;
    private final ArtifactFilter targetFilter;

    ExampleGraphFactory(GraphBuilder<DependencyNode> graphBuilder, ArtifactFilter globalFilter, ArtifactFilter targetFilter) {
      this.graphBuilder = graphBuilder;
      this.globalFilter = globalFilter;
      this.targetFilter = targetFilter;
    }


    @Override
    public String createGraph(MavenProject project) {
      DefaultArtifact aA = new DefaultArtifact("com.example", "artifact-a", "1.0.0", SCOPE_COMPILE, "jar", "", null);
      DefaultArtifact aB = new DefaultArtifact("com.example", "artifact-b", "1.0.0", SCOPE_COMPILE, "jar", "", null);
      DefaultArtifact aC = new DefaultArtifact("com.example", "artifact-c", "2.0.0", SCOPE_COMPILE, "jar", "", null);
      DefaultArtifact aD = new DefaultArtifact("com.example", "artifact-d", "1.0.0", SCOPE_COMPILE, "jar", "", null);
      DefaultArtifact aE = new DefaultArtifact("com.example.sub", "artifact-e", "1.0.0", SCOPE_PROVIDED, "jar", "", null);
      DefaultArtifact aF = new DefaultArtifact("com.example.sub", "artifact-f", "1.0.0", SCOPE_RUNTIME, "jar", "", null);
      DefaultArtifact aG = new DefaultArtifact("com.example.sub", "artifact-g", "1.0.0", SCOPE_TEST, "jar", "", null);
      DefaultArtifact aZ = new DefaultArtifact("com.example.sub", "artifact-zip", "1.0.0", SCOPE_COMPILE, "zip", "", null);

      DependencyNode nA = new DependencyNode(aA);
      DependencyNode nB = new DependencyNode(aB);
      DependencyNode nC = new DependencyNode(aC);
      DependencyNode nCDup = createConflict(aC, aC.getVersion());
      DependencyNode nCConfl = createConflict(aC, "1.0.0");
      DependencyNode nD = new DependencyNode(aD);
      DependencyNode nE = new DependencyNode(aE);
      DependencyNode nF = new DependencyNode(aF);
      DependencyNode nG = new DependencyNode(aG);
      DependencyNode nZ = new DependencyNode(aZ);

      addEdge(nA, nB);
      addEdge(nA, nD);
      addEdge(nG, nCConfl);
      addEdge(nB, nC);
      addEdge(nB, nD);
      addEdge(nZ, nCDup);
      addEdge(nD, nE);
      addEdge(nD, nF);
      addEdge(nB, nG);
      addEdge(nB, nZ);

      return this.graphBuilder.toString();
    }

    private void addEdge(DependencyNode from, DependencyNode to) {
      if (this.globalFilter.include(from.getArtifact())
          && this.globalFilter.include(to.getArtifact())
          && this.targetFilter.include(to.getArtifact())) {

        this.graphBuilder.addEdge(from, to);
      }
    }

    private static DependencyNode createConflict(Artifact artifact, String winningVersion) {
      org.eclipse.aether.artifact.DefaultArtifact aetherArtifact = new org.eclipse.aether.artifact.DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), artifact.getType(), artifact.getVersion());
      org.eclipse.aether.artifact.DefaultArtifact winnerArtifact = new org.eclipse.aether.artifact.DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), artifact.getType(), winningVersion);

      DefaultDependencyNode dependencyNode = new DefaultDependencyNode(new Dependency(aetherArtifact, artifact.getScope()));
      dependencyNode.setData(NODE_DATA_WINNER, new DefaultDependencyNode(new Dependency(winnerArtifact, "compile")));

      return new DependencyNode(dependencyNode);
    }

  }
}
