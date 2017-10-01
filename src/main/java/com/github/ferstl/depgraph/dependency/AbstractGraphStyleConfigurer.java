package com.github.ferstl.depgraph.dependency;

abstract class AbstractGraphStyleConfigurer implements GraphStyleConfigurer {

  boolean showGroupId;
  boolean showArtifactId;
  boolean showVersionsOnNodes;
  boolean showTypes;
  boolean showClassifiers;
  boolean showVersionOnEdges;

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
  public GraphStyleConfigurer showTypes(boolean showTypes) {
    this.showTypes = showTypes;
    return this;
  }

  @Override
  public GraphStyleConfigurer showClassifiers(boolean showClassifiers) {
    this.showClassifiers = showClassifiers;
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
}
