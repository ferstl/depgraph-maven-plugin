package com.github.ferstl.depgraph.dot;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.containsInAnyOrder;


public final class DotBuilderMatcher extends TypeSafeDiagnosingMatcher<DotBuilder> {

  private static final String NODE_PATTERN = ".+\"\\[label=\".+\"\\]";
  private static final String EDGE_PATTERN = "\".+\" -> \".+\"(\\[.+\\])?";

  private final String[] expectedNodes;
  private final String[] expectedEdges;

  private List<String> nodes;
  private List<String> edges;



  private DotBuilderMatcher(String[] expectedNodes, String[] expectedEdges) {
    this.expectedNodes = expectedNodes;
    this.expectedEdges = expectedEdges;
  }


  public static DotBuilderMatcher contains(String[] nodes, String[] edges) {
    return new DotBuilderMatcher(nodes, edges);
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
  protected boolean matchesSafely(DotBuilder dotBuilder, Description mismatchDescription) {
    mismatchDescription.appendText("was");
    boolean result = true;

    init(dotBuilder);

    if (!containsInAnyOrder(this.expectedNodes).matches(this.nodes)) {
      mismatchDescription.appendText("\nNodes:");
      for (String node : this.nodes) {
        mismatchDescription.appendText("\n").appendText(node);
      }

      result = false;
    }

    if (!containsInAnyOrder(this.expectedEdges).matches(this.edges)) {
      mismatchDescription.appendText("\nEdges:");
      for (String edge : this.edges) {
        mismatchDescription.appendText("\n").appendText(edge);
      }

      result = false;
    }

    return result;
  }

  private void init(DotBuilder dotBuilder) {
    String graph = dotBuilder.toString();
    String[] lines = graph.split("\n");
    this.nodes = new ArrayList<>();
    this.edges = new ArrayList<>();

    for (String line : lines) {
      String trimmed = StringUtils.trim(line);

      if (trimmed.matches(NODE_PATTERN)) {
        this.nodes.add(trimmed);
      } else if (trimmed.matches(EDGE_PATTERN)) {
        this.edges.add(trimmed);
      }
    }
  }

}
