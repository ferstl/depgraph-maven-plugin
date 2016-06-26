package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Polygon extends AbstractNode {

  private int sides;

  Polygon() {
    super("polygon");
  }

  @Override
  AttributeBuilder createAttributes() {
    return super.createAttributes().addAttribute("sides", this.sides > 0 ? Integer.toString(this.sides) : null);
  }

}
