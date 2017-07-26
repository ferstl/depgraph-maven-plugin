package com.github.ferstl.depgraph;

import com.github.ferstl.depgraph.graph.NodeRenderer;

public enum ToStringNodeIdRenderer implements NodeRenderer<Object> {
  INSTANCE;

  @Override
  public String render(Object node) {
    return node.toString();
  }
}
