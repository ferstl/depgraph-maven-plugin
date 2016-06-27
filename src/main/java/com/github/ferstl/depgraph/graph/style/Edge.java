package com.github.ferstl.depgraph.graph.style;

import org.apache.commons.lang3.StringUtils;
import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Edge {

  private String style;
  private String color;
  private final Font font = new Font();

  AttributeBuilder createAttributes() {
    AttributeBuilder builder = new AttributeBuilder()
        .style(this.style)
        .color(this.color);

    return getFont().setAttributes(builder);
  }

  private Font getFont() {
    return this.font != null ? this.font : new Font();
  }

  void merge(Edge other) {
    this.style = StringUtils.defaultIfBlank(other.style, this.style);
    this.color = StringUtils.defaultIfBlank(other.color, this.color);
    this.font.merge(other.font);
  }
}
