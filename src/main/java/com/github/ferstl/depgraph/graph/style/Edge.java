package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Edge {

  String style;
  String color;
  Font font;

  public AttributeBuilder createAttributes() {
    AttributeBuilder builder = new AttributeBuilder()
        .style(this.style)
        .color(this.color);

    return getFont().setAttributes(builder);
  }

  private Font getFont() {
    return this.font != null ? this.font : new Font();
  }
}
