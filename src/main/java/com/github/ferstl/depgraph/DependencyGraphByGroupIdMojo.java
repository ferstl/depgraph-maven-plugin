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
package com.github.ferstl.depgraph;

import java.util.Set;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.MavenGraphAdapter;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import com.github.ferstl.depgraph.dependency.SimpleGraphFactory;
import com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource;
import com.github.ferstl.depgraph.graph.GraphBuilder;

import static java.util.EnumSet.allOf;

/**
 * Creates a graph containing the group IDs of all dependencies.
 */
@Mojo(
    name = "by-groupid",
    aggregator = false,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.TEST,
    threadSafe = true)
public class DependencyGraphByGroupIdMojo extends AbstractGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {

    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
        .showGroupIds(true)
        .showArtifactIds(false)
        .showTypes(false)
        .showClassifiers(false)
        .showVersionsOnNodes(false)
        .showVersionsOnEdges(false)
        .configure(GraphBuilder.<DependencyNode>create(DependencyNodeIdRenderer.groupId().withScope(true)))
        .omitSelfReferences();

    MavenGraphAdapter adapter = new MavenGraphAdapter(this.dependencyTreeBuilder, this.localRepository, transitiveFilter, targetFilter, allOf(NodeResolution.class));
    return new SimpleGraphFactory(adapter, globalFilter, graphBuilder);
  }

  @Override
  protected Set<BuiltInStyleResource> getAdditionalStyleResources() {
    Set<BuiltInStyleResource> resources = super.getAdditionalStyleResources();
    resources.add(BuiltInStyleResource.GROUP_ID_ONLY_STYLE);

    return resources;
  }
}
