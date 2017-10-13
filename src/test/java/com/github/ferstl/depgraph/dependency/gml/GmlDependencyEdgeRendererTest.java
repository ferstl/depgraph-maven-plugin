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
package com.github.ferstl.depgraph.dependency.gml;

import com.github.ferstl.depgraph.dependency.AbstractDependencyEdgeRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.EdgeRenderer;

public class GmlDependencyEdgeRendererTest extends AbstractDependencyEdgeRendererTest {

  @Override
  protected EdgeRenderer<DependencyNode> createEdgeRenderer(boolean renderVersion) {
    return new GmlDependencyEdgeRenderer(renderVersion);
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
    return "label \"version2\"\n"
        + "graphics\n"
        + "[\n"
        + "style \"dashed\"\n"
        + "targetArrow \"standard\"\n"
        + "fill \"#FF0000\"\n"
        + "]\n"
        + "LabelGraphics\n"
        + "[\n"
        + "color \"#FF0000\"\n"
        + "]";
  }

  @Override
  protected String renderWithConflictNotShowingVersionResult() {
    return "\n"
        + "graphics\n"
        + "[\n"
        + "style \"dashed\"\n"
        + "targetArrow \"standard\"\n"
        + "fill \"#FF0000\"\n"
        + "]\n"
        + "LabelGraphics\n"
        + "[\n"
        + "color \"#FF0000\"\n"
        + "]";
  }
}
