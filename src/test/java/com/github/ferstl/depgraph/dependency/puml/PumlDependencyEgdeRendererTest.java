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
package com.github.ferstl.depgraph.dependency.puml;

import org.junit.Test;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeUtil;

import static org.junit.Assert.assertEquals;

public class PumlDependencyEgdeRendererTest {

  private final PumlDependencyEgdeRenderer renderer = new PumlDependencyEgdeRenderer();

  @Test
  public void testRenderEdgeToIncludeDependency() throws Exception {
    DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-context", "4.3.9-RELEASE");

    DependencyNode toNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-core", "4.3.9-RELEASE");

    String edgeInfo = this.renderer.render(fromNode, toNode);

    assertEquals("{\"begin\":\"-[\",\"end\":\"]->\",\"color\":\"#000000\",\"label\":\"\"}", edgeInfo);

  }

  @Test
  public void testRenderEdgeToConflictingDependency() throws Exception {
    DependencyNode fromNode = DependencyNodeUtil.createDependencyNode("org.springframework",
        "spring-context", "4.3.9-RELEASE");

    DependencyNode toNode = DependencyNodeUtil.createDependencyNodeWithConflict("commons-logging",
        "commons-logging", "1.1.3");

    String edgeInfo = this.renderer.render(fromNode, toNode);

    assertEquals("{\"begin\":\".[\",\"end\":\"].>\",\"color\":\"#FF0000\",\"label\":\"1.1.3\"}", edgeInfo);
  }
}
