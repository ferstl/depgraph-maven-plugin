package com.github.ferstl.depgraph;

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

/**
 * Creates a graph that shows the build execution tree of a maven project. The build execution tree is helpful to
 * analyze which modules can be built in parallel and which modules depend on each other when building the project.
 *
 * @see <a href="https://cwiki.apache.org/confluence/display/MAVEN/Parallel+builds+in+Maven+3"/>
 * @since 4.0.0
 */
@Mojo(
    name = "build-tree",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.NONE,
    threadSafe = true)
public class BuildTreeMojo extends AbstractGraphMojo {

  /**
   * If set to {@code true}, the created graph will show the {@code groupId} on all modules.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showGroupIds", defaultValue = "false")
  private boolean showGroupIds;

  /**
   * If set to {@code true}, the created graph will show versions on all modules.
   *
   * @since 4.0.0
   */
  @Parameter(property = "showVersions", defaultValue = "false")
  private boolean showVersions;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Override
  protected BuildTreeGraphFactory createGraphFactory(GraphStyleConfigurer graphStyleConfigurer) {
    DependencyNodeIdRenderer nodeIdRenderer = DependencyNodeIdRenderer.versionlessId();

    GraphBuilder<DependencyNode> graphBuilder = graphStyleConfigurer
        .showGroupIds(this.showGroupIds)
        .showArtifactIds(true)
        .showVersionsOnNodes(this.showVersions)
        .configure(GraphBuilder.create(nodeIdRenderer));

    return new BuildTreeGraphFactory(graphBuilder, this.mavenSession.getProjectDependencyGraph());
  }
}
