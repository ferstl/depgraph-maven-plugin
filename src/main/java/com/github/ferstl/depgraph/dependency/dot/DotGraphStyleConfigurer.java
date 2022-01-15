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
package com.github.ferstl.depgraph.dependency.dot;


import com.github.ferstl.depgraph.dependency.AbstractGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.dot.DotGraphFormatter;

public class DotGraphStyleConfigurer extends AbstractGraphStyleConfigurer {

  private final StyleConfiguration styleConfiguration;

  public DotGraphStyleConfigurer(StyleConfiguration styleConfiguration) {
    this.styleConfiguration = styleConfiguration;
  }

  @Override
  public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
    DotDependencyNodeNameRenderer nodeNameRenderer = new DotDependencyNodeNameRenderer(this.showGroupId, this.showArtifactId, this.showTypes, this.showClassifiers, this.showVersionsOnNodes, this.showOptional, this.showScope, this.styleConfiguration);
    DotDependencyEdgeRenderer edgeRenderer = new DotDependencyEdgeRenderer(this.showVersionOnEdges, this.styleConfiguration);

    return graphBuilder
        .graphFormatter(new DotGraphFormatter(this.styleConfiguration.graphAttributes(), this.styleConfiguration.defaultNodeAttributes(), this.styleConfiguration.defaultEdgeAttributes()))
        .useNodeNameRenderer(nodeNameRenderer)
        .useEdgeRenderer(edgeRenderer);
  }
}
