package com.github.ferstl.depgraph;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.ScopeArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternExcludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;

/**
 * Abstract mojo to create <strong>dependency</strong> graphs. It enhances the creation of {@link GraphFactory} instances
 * with various filters for dependencies.
 */
abstract class AbstractDependencyGraphMojo extends AbstractGraphMojo {

  /**
   * The scope of the artifacts that should be included in the graph. An empty string indicates all scopes (default).
   * The scopes being interpreted are the scopes as Maven sees them, not as specified in the pom. In summary:
   * <ul>
   * <li>{@code compile}: Shows compile, provided and system dependencies</li>
   * <li>{@code provided}: Shows provided dependencies</li>
   * <li>{@code runtime}: Shows compile and runtime dependencies</li>
   * <li>{@code system}: Shows system dependencies</li>
   * <li>{@code test} (default): Shows all dependencies</li>
   * </ul>
   *
   * @since 1.0.0
   */
  @Parameter(property = "scope")
  private String scope;
  /**
   * List of artifacts to be included in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "includes")
  private List<String> includes;
  /**
   * List of artifacts to be excluded in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "excludes")
  private List<String> excludes;
  /**
   * List of artifacts in the form of {@code groupId:artifactId:type:classifier} to be included if they are
   * <strong>transitive</strong>.
   *
   * @since 3.0.0
   */
  @Parameter(property = "transitiveIncludes")
  private List<String> transitiveIncludes;
  /**
   * List of artifacts in the form of {@code groupId:artifactId:type:classifier} to be excluded if they are
   * <strong>transitive</strong>.
   *
   * @since 3.0.0
   */
  @Parameter(property = "transitiveExcludes")
  private List<String> transitiveExcludes;
  /**
   * List of artifacts, in the form of {@code groupId:artifactId:type:classifier}, to restrict the dependency graph
   * only to artifacts that depend on them.
   *
   * @since 1.0.4
   */
  @Parameter(property = "targetIncludes")
  private List<String> targetIncludes;
  /**
   * Indicates whether optional dependencies should be excluded from the graph.
   *
   * @since 3.2.0
   */
  @Parameter(property = "excludeOptionalDependencies", defaultValue = "false")
  private boolean excludeOptionalDependencies;

  @Override
  protected final GraphFactory createGraphFactory(GraphStyleConfigurer graphStyleConfigurer) {
    ArtifactFilter globalFilter = createGlobalArtifactFilter();
    ArtifactFilter transitiveIncludeExcludeFilter = createTransitiveIncludeExcludeFilter();
    ArtifactFilter targetFilter = createTargetArtifactFilter();

    return createGraphFactory(globalFilter, transitiveIncludeExcludeFilter, targetFilter, graphStyleConfigurer);
  }

  protected abstract GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer);

  private ArtifactFilter createGlobalArtifactFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (this.scope != null) {
      filter.add(new ScopeArtifactFilter(this.scope));
    }

    if (!this.includes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.includes));
    }

    if (!this.excludes.isEmpty()) {
      filter.add(new StrictPatternExcludesArtifactFilter(this.excludes));
    }

    if (this.excludeOptionalDependencies) {
      filter.add(new OptionalArtifactFilter());
    }

    return filter;
  }

  private ArtifactFilter createTransitiveIncludeExcludeFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (!this.transitiveIncludes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.transitiveIncludes));
    }

    if (!this.transitiveExcludes.isEmpty()) {
      filter.add(new StrictPatternExcludesArtifactFilter(this.transitiveExcludes));
    }

    return filter;
  }

  private ArtifactFilter createTargetArtifactFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (!this.targetIncludes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.targetIncludes));
    }

    return filter;
  }

  private static class OptionalArtifactFilter implements ArtifactFilter {

    @Override
    public boolean include(Artifact artifact) {
      return !artifact.isOptional();
    }
  }
}
