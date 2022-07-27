/*
 * Copyright (c) 2014 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;
import static org.apache.maven.artifact.Artifact.SCOPE_PROVIDED;
import static org.apache.maven.artifact.Artifact.SCOPE_RUNTIME;
import static org.apache.maven.artifact.Artifact.SCOPE_SYSTEM;
import static org.apache.maven.artifact.Artifact.SCOPE_TEST;

/**
 * Abstract mojo that is intended to create dependency graphs. It adds dependency-related filtering
 * capabilities to the base implementation.
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
   * @deprecated Use {@link #classpathScope} instead.
   */
  @Deprecated
  @Parameter(property = "scope")
  private String scope;

  /**
   * The scope of the artifacts that should be included in the graph. An empty string indicates all scopes (default).
   * The scopes being interpreted are the scopes as Maven sees them, not as specified in the pom. In summary:
   * <ul>
   * <li>{@code compile}: Equivalent to `-Dscopes=compile,provided,system`</li>
   * <li>{@code provided}: Equivalent to `-Dscopes=provided`</li>
   * <li>{@code runtime}: Equivalent to `-Dscopes=compile,runtime`</li>
   * <li>{@code system}: Equivalent to `-Dscopes=system`</li>
   * <li>{@code test} (default): Shows all dependencies</li>
   * </ul>
   * This parameter replaces the former {@code scope} parameter which was introduced in version 1.0.0.
   *
   * @since 4.0.0
   */
  @Parameter(property = "classpathScope")
  private String classpathScope;

  /**
   * List of dependency scopes to be included in the graph. If empty, all scopes are included.
   *
   * @since 4.0.0
   */
  @Parameter(property = "scopes")
  private List<String> scopes;

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
      getLog().warn("The 'scope' parameter is deprecated and will be removed in future versions. Use 'classpathScope' instead.");
      // Prefer the new parameter if it is set
      if (this.classpathScope == null) {
        this.classpathScope = this.scope;
      }
    }

    if (this.classpathScope != null) {
      if (this.scopes.isEmpty()) {
        filter.add(new ScopeArtifactFilter(this.classpathScope));
      } else {
        getLog().warn("Both 'classpathScope' (formerly 'scope') and 'scopes' parameters are set. The 'classpathScope' parameter will be ignored.");
      }
    }

    if (!this.scopes.isEmpty()) {
      filter.add(createScopesArtifactFilter(this.scopes));
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

  private ArtifactFilter createScopesArtifactFilter(List<String> scopes) {
    ScopeArtifactFilter filter = new ScopeArtifactFilter();
    for (String scope : scopes) {
      if (SCOPE_COMPILE.equals(scope)) {
        filter.setIncludeCompileScope(true);
      } else if (SCOPE_RUNTIME.equals(scope)) {
        filter.setIncludeRuntimeScope(true);
      } else if (SCOPE_TEST.equals(scope)) {
        filter.setIncludeTestScope(true);
      } else if (SCOPE_PROVIDED.equals(scope)) {
        filter.setIncludeProvidedScope(true);
      } else if (SCOPE_SYSTEM.equals(scope)) {
        filter.setIncludeSystemScope(true);
      } else {
        getLog().warn("Unknown scope: " + scope);
      }
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
