package com.github.ferstl.depgraph.dot;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.junit.Before;
import org.junit.Test;

import com.github.ferstl.depgraph.DependencyNodeAdapter;

import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.emptyGraph;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodes;
import static com.github.ferstl.depgraph.dot.DotBuilderMatcher.hasNodesAndEdges;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for {@link DotBuilder}.
 */
public class DotBuilderTest {

  private static final String DEFAULT_FROM_NODE = "\"group:from:jar:1.0.0:compile\"[label=\"group:from:jar:1.0.0:compile\"]";
  private static final String DEFAULT_TO_NODE = "\"group:to:jar:1.0.0:compile\"[label=\"group:to:jar:1.0.0:compile\"]";
  private static final String DEFAULT_SINGLE_NODE = "\"group:start:jar:1.0.0:compile\"[label=\"group:start:jar:1.0.0:compile\"]";
  private static final String DEFAULT_EDGE = "\"group:from:jar:1.0.0:compile\" -> \"group:to:jar:1.0.0:compile\"";

  private DotBuilder dotBuilder;

  @Before
  public void before() {
    this.dotBuilder = new DotBuilder();
  }

  @Test
  public void graphStructure() {
    String graph = this.dotBuilder.toString();
    assertThat(graph, startsWith("digraph G {"));
    assertThat(graph, endsWith("}"));
  }

  @Test
  public void defaults() {
    Node from = createNode("from");
    Node to = createNode("to");

    this.dotBuilder.addEdge(from, to);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] { DEFAULT_FROM_NODE, DEFAULT_TO_NODE },
        new String[] { DEFAULT_EDGE }));

  }

  @Test
  public void nullNodes() {
    Node node = createNode("node");

    this.dotBuilder.addEdge(node, null);
    assertThat(this.dotBuilder, emptyGraph());

    this.dotBuilder.addEdge(null, node);
    assertThat(this.dotBuilder, emptyGraph());
  }

  @Test
  public void omitSelfReferences() {
    this.dotBuilder
      .omitSelfReferences()
      .addEdge(createNode("start"), createNode("start"));

    assertThat(this.dotBuilder, hasNodes(DEFAULT_SINGLE_NODE));

    this.dotBuilder.addEdge(createNode("from"), createNode("to"));
    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] { DEFAULT_SINGLE_NODE, DEFAULT_FROM_NODE, DEFAULT_TO_NODE },
        new String[] { DEFAULT_EDGE }));
  }


  @Test
  public void customNodeRenderer() {
    this.dotBuilder.useNodeRenderer(TestRenderer.INSTANCE);

    this.dotBuilder.addEdge(createNode("from"), createNode("to"));

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] {
            "from[label=\"group:from:jar:1.0.0:compile\"]",
            "to[label=\"group:to:jar:1.0.0:compile\"]"},
        new String[] {"from -> to"}));
  }

  @Test
  public void customEdgeRenderer() {
    this.dotBuilder
      .useEdgeRenderer(TestRenderer.INSTANCE)
      .addEdge(createNode("from"), createNode("to"));

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] { DEFAULT_FROM_NODE, DEFAULT_TO_NODE },
        new String[] { DEFAULT_EDGE + "[label=\"1.0.0\"]" }));
  }

  @Test
  public void addEdgeWithCustomRenderer() {
    this.dotBuilder.addEdge(createNode("from"), createNode("to"), TestRenderer.INSTANCE);

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] { DEFAULT_FROM_NODE, DEFAULT_TO_NODE },
        new String[] { DEFAULT_EDGE + "[label=\"1.0.0\"]" }));

  }

  @Test
  public void customNodeLabelRenderer() {
    this.dotBuilder
      .useNodeLabelRenderer(TestRenderer.INSTANCE)
      .addEdge(createNode("from"), createNode("to"));

    assertThat(this.dotBuilder, hasNodesAndEdges(
        new String[] {
          "\"group:from:jar:1.0.0:compile\"[label=from]",
          "\"group:to:jar:1.0.0:compile\"[label=to]"},
        new String[] {DEFAULT_EDGE}));
  }


  private Node createNode(String name) {
    Artifact artifact = new DefaultArtifact("group", name, "1.0.0", "compile", "jar", "", null);

    return new DependencyNodeAdapter(artifact);
  }

  static enum TestRenderer implements EdgeRenderer, NodeRenderer {
    INSTANCE;

    @Override
    public String render(Node node) {
      return node.getArtifact().getArtifactId();
    }

    @Override
    public String createEdgeAttributes(Node from, Node to) {
      return new AttributeBuilder()
        .label(to.getArtifact().getVersion())
        .toString();
    }

  }
}
