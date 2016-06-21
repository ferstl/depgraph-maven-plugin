package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Edge {

  String style;
  String color;
  Font font;

  public void setAttributes(AttributeBuilder builder) {
    builder
        .style(this.style)
        .color(this.color);

    if (this.font != null) {
      this.font.setAttributes(builder);
    }
  }
}