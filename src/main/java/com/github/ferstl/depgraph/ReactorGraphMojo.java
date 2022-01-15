package com.github.ferstl.depgraph;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.dependency.DependencyNodeIdRenderer;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.ReactorGraphFactory;
import com.github.ferstl.depgraph.graph.GraphBuilder;

/**
 * @since 4.0.0
 */
@Mojo(
    name = "reactor",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    requiresDependencyCollection = ResolutionScope.NONE,
    threadSafe = true)
public class ReactorGraphMojo extends AbstractGraphMojo {

  /**
   * If set to {@code true}, the created graph will show the {@code groupId} on all artifacts.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showGroupIds", defaultValue = "false")
  private boolean showGroupIds;

  /**
   * If set to {@code true}, the created graph will show version information an all artifacts.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showVersions", defaultValue = "false")
  private boolean showVersions;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Override
  protected GraphFactory createGraphFactory(GraphStyleConfigurer graphStyleConfigurer) {
    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId();
    GraphBuilder<DependencyNode> graphBuilder = createGraphBuilder(graphStyleConfigurer, nodeIdRenderer);

    return new ReactorGraphFactory(this.mavenSession.getProjectDependencyGraph(), graphBuilder, nodeIdRenderer);
  }

  private GraphBuilder<DependencyNode> createGraphBuilder(GraphStyleConfigurer graphStyleConfigurer, DependencyNodeIdRenderer nodeIdRenderer) {

    return graphStyleConfigurer
        .showGroupIds(this.showGroupIds)
        .showArtifactIds(true)
        .showVersionsOnNodes(this.showVersions)
        .configure(GraphBuilder.create(nodeIdRenderer));
  }
}
