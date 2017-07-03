package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.GraphBuilder;
import com.github.ferstl.depgraph.graph.puml.PumlGraphFormatter;

/**
 * @author gushakov
 */
public class PumlGraphStyleConfigurer implements GraphStyleConfigurer {

    private boolean showGroupId;
    private boolean showArtifactId;
    private boolean showVersionsOnNodes;
    private boolean showVersionOnEdges;

    @Override
    public GraphStyleConfigurer showGroupIds(boolean showGroupId) {
        this.showGroupId = showGroupId;
        return this;
    }

    @Override
    public GraphStyleConfigurer showArtifactIds(boolean showArtifactId) {
        this.showArtifactId = showArtifactId;
        return this;
    }

    @Override
    public GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes) {
        this.showVersionsOnNodes = showVersionsOnNodes;
        return this;
    }

    @Override
    public GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges) {
        this.showVersionOnEdges = showVersionOnEdges;
        return this;
    }

    @Override
    public GraphBuilder<DependencyNode> configure(GraphBuilder<DependencyNode> graphBuilder) {
        return graphBuilder
                .useNodeNameRenderer(new PumlDependencyNodeNameRenderer(this.showGroupId,
                        this.showArtifactId, this.showVersionsOnNodes))
                .useEdgeRenderer(new PumlDependencyEgdeRenderer())
                .graphFormatter(new PumlGraphFormatter());
    }

}
