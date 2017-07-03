package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.NodeRenderer;
import org.apache.maven.artifact.Artifact;

/**
 * @author gushakov
 */
public class PumlDependencyNodeNameRenderer implements NodeRenderer<DependencyNode> {

    private boolean showGroupId;
    private boolean showArtifactId;
    private boolean showVersion;

    public PumlDependencyNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showVersion) {
        this.showGroupId = showGroupId;
        this.showArtifactId = showArtifactId;
        this.showVersion = showVersion;
    }

    @Override
    public String render(DependencyNode node) {
        final Artifact artifact = node.getArtifact();

        final StringBuilder name = new StringBuilder();

        final PumlNodeInfo nodeInfo = new PumlNodeInfo().withComponent("rectangle");


        if (showGroupId) {
            name.append(artifact.getGroupId());
        }

        if (showArtifactId) {
            if (showGroupId) {
                name.append(":");
            }
            name.append(artifact.getArtifactId());
        }

        if (showVersion) {
            if (showGroupId || showArtifactId) {
                name.append(":");
            }
            name.append(artifact.getVersion());
        }

        nodeInfo.withLabel(name.toString())
                .withStereotype(node.getArtifact().getScope());

        return nodeInfo.toString();
    }
}
