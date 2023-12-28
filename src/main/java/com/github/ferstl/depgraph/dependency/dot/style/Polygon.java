/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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

import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;

class Polygon extends AbstractNode {

  private int sides;

  Polygon() {
    super("polygon");
  }

  @Override
  DotAttributeBuilder createAttributes() {
    return super.createAttributes().addAttribute("sides", this.sides > 0 ? Integer.toString(this.sides) : null);
  }

}
