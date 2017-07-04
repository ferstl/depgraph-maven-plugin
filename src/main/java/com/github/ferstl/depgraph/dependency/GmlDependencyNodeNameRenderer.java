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
package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.NodeRenderer;
import com.google.common.base.Joiner;

public class GmlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;

  public GmlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
  }

  @Override
  public String render(DependencyNode node) {
    String content = NEWLINE_JOINER.join(
        this.showGroupId ? node.getArtifact().getGroupId() : null,
        this.showArtifactId ? node.getArtifact().getArtifactId() : null,
        this.showVersion ? node.getEffectiveVersion() : null);

    if (content.isEmpty()) {
      return "";
    }

    return "label \"" + content + "\"";
  }
}
