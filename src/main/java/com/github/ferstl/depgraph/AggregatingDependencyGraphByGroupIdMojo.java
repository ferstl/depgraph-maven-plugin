/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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

import java.util.EnumSet;
import java.util.Set;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.AggregatingGraphFactory;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.MavenGraphAdapter;
import com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import static com.github.ferstl.depgraph.dependency.NodeResolution.INCLUDED;

/**
 * Aggregates all dependencies of a multi-module by their group IDs.
 */
@Mojo(
    name = "aggregate-by-groupid",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    threadSafe = true)
public class AggregatingDependencyGraphByGroupIdMojo extends AbstractAggregatingGraphMojo {

  @Override
  protected GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {

    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer
        .groupId()
        .withScope(!this.mergeScopes);

    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
        .showGroupIds(true)
        .showArtifactIds(false)
        .showTypes(false)
        .showClassifiers(false)
        .showVersionsOnNodes(false)
        .showVersionsOnEdges(false)
        .showOptional(false)
        .repeatTransitiveDependencies(this.repeatTransitiveDependenciesInTextGraph)
        .configure(GraphBuilder.create(nodeIdRenderer))
        .omitSelfReferences();

    MavenGraphAdapter adapter = new MavenGraphAdapter(this.dependenciesResolver, transitiveIncludeExcludeFilter, targetFilter, EnumSet.of(INCLUDED));

    return new AggregatingGraphFactory(adapter, subProjectsInReactorOrder(), globalFilter, graphBuilder, true, this.reduceEdges);
  }

  @Override
  protected Set<BuiltInStyleResource> getAdditionalStyleResources() {
    Set<BuiltInStyleResource> resources = super.getAdditionalStyleResources();
    resources.add(BuiltInStyleResource.GROUP_ID_ONLY_STYLE);

    return resources;
  }
}
