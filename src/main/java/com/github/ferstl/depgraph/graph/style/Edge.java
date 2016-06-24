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

    getFont().setAttributes(builder);
  }

  private Font getFont() {
    return this.font != null ? this.font : new Font();
  }
}
