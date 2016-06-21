package com.github.ferstl.depgraph.graph;

import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;


public class DependencyNodeLabelRenderer implements NodeRenderer<GraphNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;
  private final StyleConfiguration styleConfiguration;

  public DependencyNodeLabelRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion, StyleConfiguration styleConfiguration) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
    this.styleConfiguration = styleConfiguration;
  }

  @Override
  public String render(GraphNode node) {
    Artifact artifact = node.getArtifact();
    String scopes = createScopeString(node.getScopes());

    return this.styleConfiguration.renderNode(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? artifact.getVersion() : null,
        scopes,
        Iterables.getFirst(node.getScopes(), null));
  }

  private static String createScopeString(Set<String> scopes) {
    if (scopes.size() > 1 || !scopes.contains("compile")) {
      return "(" + SLASH_JOINER.join(scopes) + ")";
    }

    return "";
  }

}
