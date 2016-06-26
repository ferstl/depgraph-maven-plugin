package com.github.ferstl.depgraph.graph.style;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.LabelBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonTypeIdResolver(NodeTypeResolver.class)
abstract class AbstractNode {

  private final String type;
  private String color;
  private String style;
  private Font defaultFont;
  private Font groupIdFont;
  private Font artifactIdFont;
  private Font versionFont;
  private Font scopeFont;

  AbstractNode(String type) {
    this.type = type;
  }

  AttributeBuilder createAttributes() {
    Font defaultFont = getDefaultFont();
    return new AttributeBuilder()
        .shape(this.type)
        .style(this.style)
        .color(this.color)
        .fontName(defaultFont.name)
        .fontSize(defaultFont.size)
        .fontColor(defaultFont.color);
  }

  AttributeBuilder createAttributes(String groupId, String artifactId, String version, String scopes, boolean includeNodeAttributes) {
    Font groupIdFont = getGroupIdFont();
    Font artifactIdFont = getArtifactIdFont();
    Font versionFont = getVersionFont();
    Font scopeFont = getScopeFont();

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

  private Font getDefaultFont() {
    return this.defaultFont != null ? this.defaultFont : new Font();
  }

  private Font getGroupIdFont() {
    return this.groupIdFont != null ? this.groupIdFont : new Font();
  }

  private Font getArtifactIdFont() {
    return this.artifactIdFont != null ? this.artifactIdFont : new Font();
  }

  private Font getVersionFont() {
    return this.versionFont != null ? this.versionFont : new Font();
  }

  private Font getScopeFont() {
    return this.scopeFont != null ? this.scopeFont : new Font();
  }
}
