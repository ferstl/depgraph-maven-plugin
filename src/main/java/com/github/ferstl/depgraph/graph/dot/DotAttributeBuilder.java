/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

import static com.github.ferstl.depgraph.graph.dot.DotEscaper.escape;


public class DotAttributeBuilder {

  private final Map<String, String> attributes;

  public DotAttributeBuilder() {
    this.attributes = new LinkedHashMap<>();
  }

  public DotAttributeBuilder label(String label) {
    if (StringUtils.startsWith(label, "<") && StringUtils.endsWith(label, ">")) {
      this.attributes.put("label", label);
      return this;
    }

    return addAttribute("label", label);
  }

  public DotAttributeBuilder fontName(String fontName) {
    return addAttribute("fontname", fontName);
  }

  public DotAttributeBuilder fontSize(int fontSize) {
    if (fontSize > 0) {
      return addAttribute("fontsize", Integer.toString(fontSize));
    } else if (fontSize < 0) {
      throw new IllegalArgumentException("Negative font size");
    }

    return this;
  }

  public DotAttributeBuilder fontSize(Integer fontSize) {
    return fontSize(fontSize != null ? fontSize : 0);
  }

  public DotAttributeBuilder fontColor(String color) {
    return addAttribute("fontcolor", color);
  }

  public DotAttributeBuilder style(String style) {
    return addAttribute("style", style);
  }

  public DotAttributeBuilder color(String color) {
    return addAttribute("color", color);
  }

  public DotAttributeBuilder fillColor(String color) {
    return addAttribute("fillcolor", color);
  }

  public DotAttributeBuilder shape(String shape) {
    return addAttribute("shape", shape);
  }

  public DotAttributeBuilder addAttribute(String key, String value) {
    if (value != null) {
      this.attributes.put(key, escape(value));
    }
    return this;
  }

  @Override
  public String toString() {
    if (this.attributes.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder("[");
    for (Entry<String, String> attribute : this.attributes.entrySet()) {
      sb.append(attribute.getKey() + "=" + attribute.getValue()).append(",");
    }

    return sb.delete(sb.length() - 1, sb.length())
        .append("]")
        .toString();
  }
}
