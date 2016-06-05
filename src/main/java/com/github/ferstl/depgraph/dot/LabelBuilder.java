package com.github.ferstl.depgraph.dot;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import com.google.common.html.HtmlEscapers;

/**
 * <a href=http://www.graphviz.org/doc/info/shapes.html#html>HTML-like labels</a>.
 */
public class LabelBuilder {

  private final StringBuilder labelBuilder = new StringBuilder();

  public LabelBuilder text(String text) {
    addText(text);
    return this;
  }

  public LabelBuilder italic(String text) {
    addText(text, "i");
    return this;
  }

  public LabelBuilder bold(String text) {
    addText(text, "b");
    return this;
  }

  public LabelBuilder underline(String text) {
    addText(text, "u");
    return this;
  }

  public LabelBuilder newLine() {
    this.labelBuilder.append("<br/>");
    return this;
  }

  public FontBuilder font() {
    return new FontBuilder();
  }

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
  }

  private void addText(String text) {
    if (StringUtils.isEmpty(text)) {
      return;
    }

    this.labelBuilder.append(HtmlEscapers.htmlEscaper().escape(text));
  }

  public class FontBuilder {

    private final Map<String, String> attributes = new TreeMap<>();

    public FontBuilder color(String color) {
      addAttribute("color", color);
      return this;
    }

    public FontBuilder size(int size) {
      if (size < 0) {
        throw new IllegalArgumentException("Font size must not be negative");
      }
      addAttribute("point-size", size);
      return this;
    }

    public FontBuilder name(String font) {
      addAttribute("face", font);
      return this;
    }

    public LabelBuilder text(String text) {
      LabelBuilder.this.addText(text, "font", this.attributes.values().toArray(new String[0]));
      return LabelBuilder.this;
    }

    private void addAttribute(String name, Object value) {
      this.attributes.put(name, name + "=\"" + value + "\"");
    }
  }
}
