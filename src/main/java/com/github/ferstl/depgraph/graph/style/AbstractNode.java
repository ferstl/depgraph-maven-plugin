package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.LabelBuilder;

abstract class AbstractNode {

  final String type;
  String color;
  String style;
  Font defaultFont;
  Font groupIdFont;
  Font artifactIdFont;
  Font versionFont;
  Font scopeFont;

  AbstractNode(String type) {
    this.type = type;
  }

  public void setAttributes(AttributeBuilder builder) {
    builder.shape(this.type)
        .style(this.style)
        .color(this.color)
        .fontName(this.defaultFont.name)
        .fontSize(this.defaultFont.size)
        .fontColor(this.defaultFont.color);
  }

  public String renderLabel(String groupId, String artifactId, String version, String scopes) {
    Font groupIdFont = this.groupIdFont != null ? this.groupIdFont : new Font();
    Font artifactIdFont = this.artifactIdFont != null ? this.artifactIdFont : new Font();
    Font versionFont = this.versionFont != null ? this.versionFont : new Font();
    Font scopeFont = this.scopeFont != null ? this.scopeFont : new Font();

    return new LabelBuilder()
        .font()
        .name(groupIdFont.name)
        .color(groupIdFont.color)
        .size(groupIdFont.size)
        .text(groupId)
        .smartNewLine()
        .font()
        .name(artifactIdFont.name)
        .color(artifactIdFont.color)
        .size(artifactIdFont.size)
        .text(artifactId)
        .smartNewLine()
        .font()
        .name(versionFont.name)
        .color(versionFont.color)
        .size(versionFont.size)
        .text(version)
        .smartNewLine()
        .font()
        .name(scopeFont.name)
        .color(scopeFont.color)
        .size(scopeFont.size)
        .text(scopes)
        .build();
  }
}