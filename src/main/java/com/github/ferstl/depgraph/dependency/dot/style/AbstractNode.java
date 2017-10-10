/*
 * Copyright (c) 2014 - 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph.dependency.dot.style;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;
import com.github.ferstl.depgraph.graph.dot.DotLabelBuilder;

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
  private final Font typeFont = new Font();
  private final Font classifierFont = new Font();
  private final Font scopeFont = new Font();

  AbstractNode(String type) {
    this.type = type;
  }

  DotAttributeBuilder createAttributes() {
    Font defaultFont = this.defaultFont;
    return new DotAttributeBuilder()
        .shape(this.type)
        .style(this.style)
        .color(this.color)
        .fillColor(this.fillColor)
        .fontName(defaultFont.name)
        .fontSize(defaultFont.size)
        .fontColor(defaultFont.color);
  }

  DotAttributeBuilder createAttributes(String groupId, String artifactId, String version, String types, String scopes, String classifiers, boolean includeNodeAttributes) {
    Font groupIdFont = this.groupIdFont;
    Font artifactIdFont = this.artifactIdFont;
    Font versionFont = this.versionFont;
    Font typeFont = this.typeFont;
    Font classiferFont = this.classifierFont;
    Font scopeFont = this.scopeFont;

    DotAttributeBuilder builder = includeNodeAttributes ? createAttributes() : new DotAttributeBuilder();
    return builder
        .label(new DotLabelBuilder()
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
            .name(typeFont.name)
            .color(typeFont.color)
            .size(typeFont.size)
            .text(types)
            .smartNewLine()
            .font()
            .name(classiferFont.name)
            .color(classiferFont.color)
            .size(classiferFont.size)
            .text(classifiers)
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
