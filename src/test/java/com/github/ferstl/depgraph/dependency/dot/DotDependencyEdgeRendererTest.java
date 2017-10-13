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
package com.github.ferstl.depgraph.dependency.dot;

import org.junit.Before;
import com.github.ferstl.depgraph.dependency.AbstractDependencyEdgeRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class DotDependencyEdgeRendererTest extends AbstractDependencyEdgeRendererTest {

  private StyleConfiguration styleConfiguration;

  @Before
  public void before() {
    this.styleConfiguration = new StyleConfiguration();
  }

  @Override
  protected EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion) {
    return new DotDependencyEdgeRenderer(renderVersion, this.styleConfiguration);
  }

  @Override
  protected String renderWithoutVersionResult() {
    return "";
  }

  @Override
  protected String renderWithNonConflictingVersionResult() {
    return "";
  }

  @Override
  protected String renderWithConflictShowingVersionResult() {
    return "[label=\"version2\"]";
  }

  @Override
  protected String renderWithConflictNotShowingVersionResult() {
    return "";
  }
}
