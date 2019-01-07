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
package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.GraphBuilder;

/**
 * API to configure the style of the dependency graph.
 */
public interface GraphStyleConfigurer {

  GraphStyleConfigurer showGroupIds(boolean showGroupId);

  GraphStyleConfigurer showArtifactIds(boolean showArtifactId);

  GraphStyleConfigurer showTypes(boolean showTypes);

  GraphStyleConfigurer showClassifiers(boolean showClassifiers);

  GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes);

  GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges);

  GraphStyleConfigurer showOptional(boolean optional);

  GraphStyleConfigurer repeatTransitiveDependencies(boolean repeatTransitiveDependencies);

  GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder);
}
