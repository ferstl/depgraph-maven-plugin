package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Polygon extends AbstractNode {

  int sides = 4;

  Polygon() {
    super("polygon");
  }

  @Override
  public void setAttributes(AttributeBuilder builder) {
    super.setAttributes(builder);
    builder.addAttribute("sides", Integer.toString(this.sides));
  }

}