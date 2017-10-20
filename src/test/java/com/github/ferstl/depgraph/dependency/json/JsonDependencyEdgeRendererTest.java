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
package com.github.ferstl.depgraph.dependency.json;

import com.github.ferstl.depgraph.dependency.AbstractDependencyEdgeRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class JsonDependencyEdgeRendererTest extends AbstractDependencyEdgeRendererTest {

  @Override
  protected EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion) {
    return new JsonDependencyEdgeRenderer(renderVersion);
  }

  @Override
  protected String renderWithoutVersionResult() {
    return "{\"resolution\":\"INCLUDED\"}";
  }

  @Override
  protected String renderWithNonConflictingVersionResult() {
    return "{\"resolution\":\"INCLUDED\"}";
  }

  @Override
  protected String renderWithConflictShowingVersionResult() {
    return "{\"version\":\"version2\",\"resolution\":\"OMITTED_FOR_CONFLICT\"}";
  }

  @Override
  protected String renderWithConflictNotShowingVersionResult() {
    return "{\"resolution\":\"OMITTED_FOR_CONFLICT\"}";
  }

  @Override
  protected String renderWithDuplicateResult() {
    return "{\"resolution\":\"OMITTED_FOR_DUPLICATE\"}";
  }
}
