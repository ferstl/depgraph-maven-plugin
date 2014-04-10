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

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.DotBuilder;

@Mojo(
    name = "aggregate",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = false,
    threadSafe = true)
public class AggregatingDependencyGraphMojo extends AbstractGraphMojo {

  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  @Parameter(property = "includeParentProjects", defaultValue = "true")
  private boolean includeParentProjects;

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter artifactFilter) {
    DotBuilder dotBuilder = new DotBuilder()
      .useNodeRenderer(NodeRenderers.VERSIONLESS_ID)
      .useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_LABEL);

    if (this.showVersions) {
      dotBuilder.useNodeLabelRenderer(NodeRenderers.ARTIFACT_ID_VERSION_LABEL);
    }

    GraphBuilderAdapter adapter = new GraphBuilderAdapter(this.dependencyGraphBuilder);

    return new AggregatingGraphFactory(adapter, artifactFilter, dotBuilder, this.includeParentProjects);
  }
}
