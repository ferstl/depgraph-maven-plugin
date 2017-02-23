/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.github.ferstl.depgraph.graph.GraphFactory;
import com.github.ferstl.depgraph.graph.GraphNode;
import com.github.ferstl.depgraph.graph.GraphStyleConfigurer;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DependencyNode;

/**
 * Creates an example graph. This Mojo has the same capabilities as the {@code graph} Mojo. So it might be useful to
 * try out different options and node stylings.
 *
 * @since 2.0.0
 */
@Mojo(
    name = "example",
    aggregator = true,
    requiresProject = true,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.NONE,
    requiresDirectInvocation = false,
    threadSafe = true)
public class ExampleGraphMojo extends DependencyGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {
    DotBuilder<GraphNode> dotBuilder = createDotBuilder(graphStyleConfigurer);
    return new ExampleGraphFactory(dotBuilder, globalFilter, targetFilter);
  }


  static class ExampleGraphFactory implements GraphFactory {

    private final DotBuilder<GraphNode> dotBuilder;
    private final ArtifactFilter globalFilter;
    private final ArtifactFilter targetFilter;

    ExampleGraphFactory(DotBuilder<GraphNode> dotBuilder, ArtifactFilter globalFilter, ArtifactFilter targetFilter) {
      this.dotBuilder = dotBuilder;
      this.globalFilter = globalFilter;
      this.targetFilter = targetFilter;
    }


    @Override
    public String createGraph(MavenProject project) {
      DefaultArtifact aA = new DefaultArtifact("com.example", "artifact-a", "1.0.0", "compile", "jar", "", null);
      DefaultArtifact aB = new DefaultArtifact("com.example", "artifact-b", "1.0.0", "compile", "jar", "", null);
      DefaultArtifact aC = new DefaultArtifact("com.example", "artifact-c", "2.0.0", "compile", "jar", "", null);
      DefaultArtifact aCV1 = new DefaultArtifact("com.example", "artifact-c", "1.0.0", "compile", "jar", "", null);
      DefaultArtifact aD = new DefaultArtifact("com.example", "artifact-d", "1.0.0", "compile", "jar", "", null);
      DefaultArtifact aE = new DefaultArtifact("com.example.sub", "artifact-e", "1.0.0", "provided", "jar", "", null);
      DefaultArtifact aF = new DefaultArtifact("com.example.sub", "artifact-f", "1.0.0", "runtime", "jar", "", null);
      DefaultArtifact aG = new DefaultArtifact("com.example.sub", "artifact-g", "1.0.0", "test", "jar", "", null);
      DefaultArtifact aZ = new DefaultArtifact("com.example.sub", "artifact-zip", "1.0.0", "compile", "zip", "", null);

      GraphNode nA = new GraphNode(aA);
      GraphNode nB = new GraphNode(aB);
      GraphNode nC = new GraphNode(aC);
      GraphNode nCDup = new GraphNode(new DependencyNode(aC, DependencyNode.OMITTED_FOR_DUPLICATE, aC));
      GraphNode nCConfl = new GraphNode(new DependencyNode(aCV1, DependencyNode.OMITTED_FOR_CONFLICT, aCV1));
      GraphNode nD = new GraphNode(aD);
      GraphNode nE = new GraphNode(aE);
      GraphNode nF = new GraphNode(aF);
      GraphNode nG = new GraphNode(aG);
      GraphNode nZ = new GraphNode(aZ);

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

      return this.dotBuilder.toString();
    }

    private void addEdge(GraphNode from, GraphNode to) {
      if (this.globalFilter.include(from.getArtifact())
          && this.globalFilter.include(to.getArtifact())
          && this.targetFilter.include(to.getArtifact())) {

        this.dotBuilder.addEdge(from, to);
      }
    }

  }
}
