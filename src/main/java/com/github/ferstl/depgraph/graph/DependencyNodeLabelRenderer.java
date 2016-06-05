package com.github.ferstl.depgraph.graph;

import java.util.Set;
import org.apache.maven.artifact.Artifact;
import com.github.ferstl.depgraph.dot.NodeRenderer;
import com.google.common.base.Joiner;
import static java.util.Arrays.asList;


public class DependencyNodeLabelRenderer implements NodeRenderer<GraphNode> {

  private static final Joiner SLASH_JOINER = Joiner.on("/").skipNulls();
  private static final Joiner NEWLINE_JOINER = Joiner.on("\n").skipNulls();

  private final boolean showGroupId;
  private final boolean showArtifactId;
  private final boolean showVersion;

  public DependencyNodeLabelRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
    this.showGroupId = showGroupId;
    this.showArtifactId = showArtifactId;
    this.showVersion = showVersion;
  }

  @Override
  public String render(GraphNode node) {
    Artifact artifact = node.getArtifact();
    String label = NEWLINE_JOINER.join(asList(
        this.showGroupId ? artifact.getGroupId() : null,
        this.showArtifactId ? artifact.getArtifactId() : null,
        this.showVersion ? artifact.getVersion() : null));

    return toScopedString(label, node.getScopes());
  }

  private static String toScopedString(String string, Set<String> scopes) {
    if (scopes.size() > 1 || !scopes.contains("compile")) {
      return string + "\n(" + SLASH_JOINER.join(scopes) + ")";
    }

    return string;
  }

}
