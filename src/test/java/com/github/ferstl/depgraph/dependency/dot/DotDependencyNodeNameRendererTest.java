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

import org.junit.Before;
import com.github.ferstl.depgraph.dependency.AbstractDependencyNodeNameRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class DotDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  private StyleConfiguration styleConfiguration;

  @Before
  public void before() {
    this.styleConfiguration = new StyleConfiguration();
  }

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion, boolean showOptional) {
    return new DotDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion, showOptional, this.styleConfiguration);
  }

  @Override
  protected String renderNothingResult() {
    return "[label=\"\"]";
  }

  @Override
  protected String renderGroupIdResult() {
    return "[label=<groupId>]";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "[label=<artifactId>]";
  }

  @Override
  protected String renderVersionResult() {
    return "[label=<version>]";
  }

  @Override
  protected String renderOptionalResult() {
    return "[label=<&lt;optional&gt;>]";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "[label=<groupId<br/>artifactId<br/>version>]";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionOptionalResult() {
    return "[label=<&lt;optional&gt;<br/>groupId<br/>artifactId<br/>version>]";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "[label=<groupId<br/>artifactId>]";
  }

  @Override
  protected String renderGroupIdArtifactIdOptionalResult() {
    return "[label=<&lt;optional&gt;<br/>groupId<br/>artifactId>]";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "[label=<groupId<br/>version>]";
  }

  @Override
  protected String renderGroupIdVersionOptionalResult() {
    return "[label=<&lt;optional&gt;<br/>groupId<br/>version>]";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "[label=<artifactId<br/>version>]";
  }

  @Override
  protected String renderArtifactIdVersionOptionalResult() {
    return "[label=<&lt;optional&gt;<br/>artifactId<br/>version>]";
  }

  @Override
  protected String renderTypesResult() {
    return "[label=<artifactId<br/>.jar/.zip>]";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "[label=<artifactId>]";
  }

  @Override
  protected String renderClassifiersResult() {
    return "[label=<artifactId<br/>classifier1/classifier2>]";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "[label=<artifactId>]";
  }

  @Override
  protected String renderAllResult() {
    return "[label=<groupId<br/>artifactId<br/>version<br/>.jar/.tar.gz/.zip<br/>classifier1/classifier2<br/>(test)>]";
  }

}
