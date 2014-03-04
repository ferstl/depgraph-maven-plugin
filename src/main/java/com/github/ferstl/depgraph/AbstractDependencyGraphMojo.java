package com.github.ferstl.depgraph;

import org.apache.maven.plugins.annotations.Parameter;


abstract class AbstractDependencyGraphMojo extends AbstractGraphMojo {

  @Parameter(property = "showVersions", defaultValue = "false")
  boolean showVersions;

  @Parameter(property = "showConflicts", defaultValue = "false")
  boolean showConflicts;

  @Parameter(property = "showDuplicates", defaultValue = "false")
  boolean showDuplicates;

  protected boolean requiresFullGraph() {
    return this.showConflicts || this.showDuplicates;
  }
}
