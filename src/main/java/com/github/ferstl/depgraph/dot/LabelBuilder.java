package com.github.ferstl.depgraph.dot;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import com.google.common.html.HtmlEscapers;

/**
 * <a href=http://www.graphviz.org/doc/info/shapes.html#html>HTML-like labels</a>.
 */
public class LabelBuilder {

  private static final String NEWLINE = "<br/>";
  private final StringBuilder labelBuilder = new StringBuilder();

  /**
   * Add the given text.
   *
   * @param text Text.
   * @return This builder.
   */
  public LabelBuilder text(String text) {
    addText(text);
    return this;
  }

  /**
   * Add italic text.
   *
   * @param text Text.
   * @return This builder.
   */
  public LabelBuilder italic(String text) {
    addText(text, "i");
    return this;
  }

  /**
   * Add bold text.
   *
   * @param text Text.
   * @return This builder.
   */
  public LabelBuilder bold(String text) {
    addText(text, "b");
    return this;
  }

  /**
   * Add underlined text.
   *
   * @param text Text.
   * @return This builder.
   */
  public LabelBuilder underline(String text) {
    addText(text, "u");
    return this;
  }

  /**
   * Add a line break.
   *
   * @return This builder.
   */
  public LabelBuilder newLine() {
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
  public LabelBuilder smartNewLine() {
    int length = this.labelBuilder.length();
    int nLength = NEWLINE.length();

    if ((length > 0 && length < nLength) || (length >= nLength && !NEWLINE.equals(this.labelBuilder.substring(length - nLength, length)))) {
      return newLine();
    }

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
  }

  private void addText(String text) {
    if (StringUtils.isEmpty(text)) {
      return;
    }

    this.labelBuilder.append(HtmlEscapers.htmlEscaper().escape(text));
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
    public LabelBuilder text(String text) {
      if (this.attributes.size() > 0) {
        LabelBuilder.this.addText(text, "font", this.attributes.values().toArray(new String[0]));
      } else {
        LabelBuilder.this.text(text);
      }

      return LabelBuilder.this;
    }

    private void addAttribute(String name, Object value) {
      this.attributes.put(name, name + "=\"" + value + "\"");
    }
  }
}
