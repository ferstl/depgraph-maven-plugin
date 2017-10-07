package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.NodeRenderer;

public class TextDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

  public TextDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersionsOnNodes) {
  }

  @Override
  public String render(DependencyNode node) {
    return "";
  }
}
