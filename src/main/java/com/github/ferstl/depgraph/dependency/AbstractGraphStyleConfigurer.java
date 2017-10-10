package com.github.ferstl.depgraph.dependency;

public abstract class AbstractGraphStyleConfigurer implements GraphStyleConfigurer {

  protected boolean showGroupId;
  protected boolean showArtifactId;
  protected boolean showVersionsOnNodes;
  protected boolean showTypes;
  protected boolean showClassifiers;
  protected boolean showVersionOnEdges;

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

  // Only relevant for the text graph. Don't do anything here.
  @Override
  public GraphStyleConfigurer repeatTransitiveDependencies(boolean repeatTransitiveDependencies) {
    return this;
  }
}
