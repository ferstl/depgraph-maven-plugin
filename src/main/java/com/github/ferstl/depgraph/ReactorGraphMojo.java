/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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

import java.util.Set;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.ReactorGraphFactory;
import com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource.REACTOR_STYLE;

/**
 * Creates a dependency graph of the Maven rector in a multimodule project.
 *
 * @since 4.0.0
 */
@Mojo(
    name = "reactor",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.NONE,
    threadSafe = true)
public class ReactorGraphMojo extends AbstractGraphMojo {

  /**
   * If set to {@code true}, the created graph will show the {@code groupId} on all artifacts.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showGroupIds", defaultValue = "false")
  private boolean showGroupIds;

  /**
   * If set to {@code true}, the created graph will show version information an all artifacts.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showVersions", defaultValue = "false")
  private boolean showVersions;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Override
  protected GraphFactory createGraphFactory(GraphStyleConfigurer graphStyleConfigurer) {
    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId();
    GraphBuilder<DependencyNode> graphBuilder = createGraphBuilder(graphStyleConfigurer, nodeIdRenderer);

    return new ReactorGraphFactory(this.mavenSession.getProjectDependencyGraph(), graphBuilder, nodeIdRenderer);
  }

  private GraphBuilder<DependencyNode> createGraphBuilder(GraphStyleConfigurer graphStyleConfigurer, DependencyNodeIdRenderer nodeIdRenderer) {

    return graphStyleConfigurer
        .showGroupIds(this.showGroupIds)
        .showArtifactIds(true)
        .showScope(false)
        .showVersionsOnNodes(this.showVersions)
        .configure(GraphBuilder.create(nodeIdRenderer));
  }

  @Override
  protected Set<BuiltInStyleResource> getAdditionalStyleResources() {
    Set<BuiltInStyleResource> additionalStyleResources = super.getAdditionalStyleResources();
    additionalStyleResources.add(REACTOR_STYLE);

    return additionalStyleResources;
  }
}
