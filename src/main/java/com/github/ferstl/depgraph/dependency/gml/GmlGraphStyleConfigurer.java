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
package com.github.ferstl.depgraph.dependency.gml;

import com.github.ferstl.depgraph.dependency.AbstractGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.github.ferstl.depgraph.graph.gml.GmlGraphFormatter;

public class GmlGraphStyleConfigurer extends AbstractGraphStyleConfigurer {

  @Override
  public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
    NodeRenderer<DependencyNode> nodeNameRenderer = new GmlDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showTypes, this.showClassifiers, this.showVersionsOnNodes, this.showOptional, this.showScope);
    EdgeRenderer<DependencyNode> edgeRenderer = new GmlDependencyEdgeRenderer(this.showVersionOnEdges);
    return graphBuilder
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer)
        .graphFormatter(new GmlGraphFormatter());
  }
}
