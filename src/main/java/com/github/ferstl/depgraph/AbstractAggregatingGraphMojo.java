package com.github.ferstl.depgraph;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractAggregatingGraphMojo extends AbstractGraphMojo {

  /**
   * Merge dependencies that occur in multiple scopes into one graph node instead of having a node per scope.
   *
   * @since 2.0.0
   */
  @Parameter(property = "mergeScopes", defaultValue = "false")
  boolean mergeScopes;

}
