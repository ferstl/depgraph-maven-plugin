package com.github.ferstl.depgraph.graph.style;

import org.apache.commons.lang3.StringUtils;
import com.github.ferstl.depgraph.dot.AttributeBuilder;

class Font {

  String color;
  Integer size;
  String name;

  AttributeBuilder setAttributes(AttributeBuilder builder) {
    return builder
        .fontColor(this.color)
        .fontSize(this.size)
        .fontName(this.name);
  }

  void merge(Font other) {
    this.color = StringUtils.defaultIfBlank(other.color, this.color);
    this.size = other.size != null ? other.size : this.size;
    this.name = StringUtils.defaultIfBlank(other.name, this.name);
  }
}
