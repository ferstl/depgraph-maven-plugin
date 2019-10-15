package com.github.ferstl.depgraph;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.BuildTreeGraphFactory;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.graph.GraphBuilder;

@Mojo(
    name = "build-tree",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.NONE,
    threadSafe = true)
public class BuildTreeMojo extends AbstractGraphMojo {

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Override
  protected BuildTreeGraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer) {
    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId();

    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
        .showArtifactIds(true)
        .showVersionsOnNodes(true)
        .configure(GraphBuilder.create(nodeIdRenderer));

    return new BuildTreeGraphFactory(graphBuilder, this.mavenSession.getProjectDependencyGraph());
  }
}
