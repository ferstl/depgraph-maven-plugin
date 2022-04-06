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
package com.github.ferstl.depgraph.graph.mermaid;

public class MermaidEdgeStyle {
    private static final String DUPLICATE_COLOR = "#000000"; // lightGray
    private static final String CONFLICT_COLOR = "#FF0000"; // red

    private int index;
    private String color;
    private int strokeSize;

    private MermaidEdgeStyle() {}

    private MermaidEdgeStyle(int index, String color, int strokeSize) {
        this.index = index;
        this.color = color;
        this.strokeSize = strokeSize;
    }

    public static MermaidEdgeStyle createConflictStyle(int index) {
        return new MermaidEdgeStyle(index, CONFLICT_COLOR, 10);
    }

    public static MermaidEdgeStyle createDuplicateStyle(int index) {
        return new MermaidEdgeStyle(index, DUPLICATE_COLOR, 3);
    }

    @Override
    public String toString() {
        return "linkStyle " + index + " color:" + color + ", stroke:" + color + ", stroke-dasharray:" + strokeSize + "px";
    }
}
