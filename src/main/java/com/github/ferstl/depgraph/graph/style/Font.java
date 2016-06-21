package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Font {

  String color;
  Integer size;
  String name;

  public void setAttributes(AttributeBuilder builder) {
    builder
        .fontColor(this.color)
        .fontSize(this.size)
        .fontName(this.name);
  }
}