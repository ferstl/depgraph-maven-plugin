package com.github.ferstl.depgraph.graph.style;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.LabelBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonTypeIdResolver(NodeTypeResolver.class)
abstract class AbstractNode {

  private final String type;
  private String color;
  private String fillColor;
  private String style;
  private final Font defaultFont = new Font();
  private final Font groupIdFont = new Font();
  private final Font artifactIdFont = new Font();
  private final Font versionFont = new Font();
  private final Font scopeFont = new Font();

  AbstractNode(String type) {
    this.type = type;
  }

  AttributeBuilder createAttributes() {
    Font defaultFont = this.defaultFont;
    return new AttributeBuilder()
        .shape(this.type)
        .style(this.style)
        .color(this.color)
        .fillColor(this.fillColor)
        .fontName(defaultFont.name)
        .fontSize(defaultFont.size)
        .fontColor(defaultFont.color);
  }

  AttributeBuilder createAttributes(String groupId, String artifactId, String version, String scopes, boolean includeNodeAttributes) {
    Font groupIdFont = this.groupIdFont;
    Font artifactIdFont = this.artifactIdFont;
    Font versionFont = this.versionFont;
    Font scopeFont = this.scopeFont;

    AttributeBuilder builder = includeNodeAttributes ? createAttributes() : new AttributeBuilder();
    return builder
        .label(new LabelBuilder()
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
            .build());
  }

  void merge(AbstractNode other) {
    this.color = StringUtils.defaultIfBlank(other.color, this.color);
    this.fillColor = StringUtils.defaultIfBlank(other.fillColor, this.fillColor);
    this.style = StringUtils.defaultIfBlank(other.style, this.style);

    this.defaultFont.merge(other.defaultFont);
    this.groupIdFont.merge(other.groupIdFont);
    this.artifactIdFont.merge(other.artifactIdFont);
    this.versionFont.merge(other.versionFont);
    this.scopeFont.merge(other.scopeFont);
  }
}
