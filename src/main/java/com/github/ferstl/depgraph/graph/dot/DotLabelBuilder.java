/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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
package com.github.ferstl.depgraph.graph.dot;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import com.google.common.html.HtmlEscapers;

/**
 * <a href=http://www.graphviz.org/doc/info/shapes.html#html>HTML-like labels</a>.
 */
public class DotLabelBuilder {

  private static final String NEWLINE = "<br/>";
  private final StringBuilder labelBuilder = new StringBuilder();
  private boolean smartNewLine;
  private boolean empty = true;

  /**
   * Add the given text.
   *
   * @param text Text.
   * @return This builder.
   */
  public DotLabelBuilder text(String text) {
    addText(text);
    return this;
  }

  /**
   * Add italic text.
   *
   * @param text Text.
   * @return This builder.
   */
  public DotLabelBuilder italic(String text) {
    addText(text, "i");
    return this;
  }

  /**
   * Add bold text.
   *
   * @param text Text.
   * @return This builder.
   */
  public DotLabelBuilder bold(String text) {
    addText(text, "b");
    return this;
  }

  /**
   * Add underlined text.
   *
   * @param text Text.
   * @return This builder.
   */
  public DotLabelBuilder underline(String text) {
    addText(text, "u");
    return this;
  }

  /**
   * Add a line break.
   *
   * @return This builder.
   */
  public DotLabelBuilder newLine() {
    this.labelBuilder.append(NEWLINE);
    return this;
  }

  /**
   * Add a line only if:
   * <ul>
   * <li>The label is not empty</li>
   * <li>The label does not already end with a line break</li>
   * </ul>
   * .
   *
   * @return This builder.
   */
  public DotLabelBuilder smartNewLine() {
    this.smartNewLine = true;
    return this;
  }

  /**
   * Configure font options.
   *
   * @return A builder to configure font options.
   */
  public FontBuilder font() {
    return new FontBuilder();
  }

  /**
   * Builds the label.
   *
   * @return The label.
   */
  public String build() {
    if (this.labelBuilder.length() == 0) {
      return "";
    }

    return "<" + this.labelBuilder + ">";
  }

  private void addText(String text, String tagName, String... tagAttributes) {
    if (StringUtils.isEmpty(text)) {
      return;
    }

    // tag with attributes
    this.labelBuilder.append("<").append(tagName);
    for (String attribute : tagAttributes) {
      this.labelBuilder.append(" ").append(attribute);
    }
    this.labelBuilder.append(">");

    // text
    addText(text);

    // end tag
    this.labelBuilder.append("</").append(tagName).append(">");

    this.empty = false;
  }

  private void addText(String text) {
    if (StringUtils.isEmpty(text)) {
      return;
    }

    if (this.smartNewLine && !this.empty) {
      newLine();
      this.smartNewLine = false;
    }
    this.labelBuilder.append(HtmlEscapers.htmlEscaper().escape(text));

    this.empty = false;
  }

  public class FontBuilder {

    private final Map<String, String> attributes = new TreeMap<>();

    /**
     * Set the font color.
     *
     * @param color Color.
     * @return This builder.
     */
    public FontBuilder color(String color) {
      addAttribute("color", color);
      return this;
    }

    /**
     * Set the font size.
     *
     * @param size Size.
     * @return This builder.
     */
    public FontBuilder size(int size) {
      if (size > 0) {
        addAttribute("point-size", size);
      } else if (size < 0) {
        throw new IllegalArgumentException("Font size must not be negative");
      }

      return this;
    }

    /**
     * Set the font size.
     *
     * @param size Size.
     * @return This builder.
     */
    public FontBuilder size(Integer size) {
      return size(size != null ? size : 0);
    }

    /**
     * Set the font name.
     *
     * @param font Font name.
     * @return This builder.
     */
    public FontBuilder name(String font) {
      addAttribute("face", font);
      return this;
    }

    /**
     * Add the given text.
     *
     * @param text Text.
     * @return The encapsulating label builder.
     */
    public DotLabelBuilder text(String text) {
      if (this.attributes.size() > 0) {
        DotLabelBuilder.this.addText(text, "font", this.attributes.values().toArray(new String[0]));
      } else {
        DotLabelBuilder.this.text(text);
      }

      return DotLabelBuilder.this;
    }

    private void addAttribute(String name, Object value) {
      if (value != null) {
        this.attributes.put(name, name + "=\"" + value + "\"");
      }
    }
  }
}
