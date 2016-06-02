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
package com.github.ferstl.depgraph.dot;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import com.github.ferstl.depgraph.GraphNode;
import static org.hamcrest.Matchers.containsInAnyOrder;


public final class DotBuilderMatcher extends TypeSafeDiagnosingMatcher<DotBuilder<GraphNode>> {

  private static final String[] EMPTY_ARRAY = new String[0];
  private static final String NODE_PATTERN = ".+\\[label=.+\\]";
  private static final String EDGE_PATTERN = ".+ -> .+";

  private final String[] expectedNodes;
  private final String[] expectedEdges;

  private List<String> nodes;
  private List<String> edges;


  private DotBuilderMatcher(String[] expectedNodes, String[] expectedEdges) {
    this.expectedNodes = expectedNodes;
    this.expectedEdges = expectedEdges;
  }


  public static DotBuilderMatcher hasNodesAndEdges(String[] nodes, String[] edges) {
    return new DotBuilderMatcher(nodes, edges);
  }

  public static DotBuilderMatcher hasNodes(String... nodes) {
    return new DotBuilderMatcher(nodes, EMPTY_ARRAY);
  }

  public static DotBuilderMatcher emptyGraph() {
    return new DotBuilderMatcher(EMPTY_ARRAY, EMPTY_ARRAY);
  }


  @Override
  public void describeTo(Description description) {
    description.appendText("Graph containing");
    if (this.expectedNodes.length != 0) {
      description.appendText("\nNodes:");
      for (String node : this.expectedNodes) {
        description.appendText("\n  ").appendText(node);
      }
    } else {
      description.appendText(" No nodes");
    }

    description.appendText("\nand");

    if (this.expectedEdges.length != 0) {
      description.appendText("\nEdges:");
      for (String edge : this.expectedEdges) {
        description.appendText("\n  ").appendText(edge);
      }
    } else {
      description.appendText(" No edges");
    }
  }


  @Override
  protected boolean matchesSafely(DotBuilder<GraphNode> dotBuilder, Description mismatchDescription) {
    init(dotBuilder);

    mismatchDescription.appendText("was\nNodes:");
    for (String node : this.nodes) {
      mismatchDescription.appendText("\n").appendText(node);
    }
    mismatchDescription.appendText("\nEdges:");
    for (String edge : this.edges) {
      mismatchDescription.appendText("\n").appendText(edge);
    }

    return containsInAnyOrder(this.expectedNodes).matches(this.nodes)
        | containsInAnyOrder(this.expectedEdges).matches(this.edges);
  }

  private void init(DotBuilder<GraphNode> dotBuilder) {
    String graph = dotBuilder.toString();
    String[] lines = graph.split("\n");
    this.nodes = new ArrayList<>();
    this.edges = new ArrayList<>();

    for (String line : lines) {
      String trimmed = StringUtils.trim(line);

      if (trimmed.matches(EDGE_PATTERN)) {
        this.edges.add(trimmed);
      } else if (trimmed.matches(NODE_PATTERN)) {
        this.nodes.add(trimmed);
      }
    }
  }

}
