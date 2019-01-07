/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;

class Edge {

  private String style;
  private String color;
  private final Font font = new Font();

  DotAttributeBuilder createAttributes() {
    DotAttributeBuilder builder = new DotAttributeBuilder()
        .style(this.style)
        .color(this.color);

    return getFont().setAttributes(builder);
  }

  private Font getFont() {
    return this.font;
  }

  void merge(Edge other) {
    this.style = StringUtils.defaultIfBlank(other.style, this.style);
    this.color = StringUtils.defaultIfBlank(other.color, this.color);
    this.font.merge(other.font);
  }
}
