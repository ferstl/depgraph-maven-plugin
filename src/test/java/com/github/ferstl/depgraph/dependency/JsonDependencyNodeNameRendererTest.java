package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.graph.NodeRenderer;

public class JsonDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion) {
    return new JsonDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion);
  }

  @Override
  protected String renderNothingResult() {
    return "{\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderGroupIdResult() {
    return "{\"groupId\":\"groupId\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderVersionResult() {
    return "{\"version\":\"version\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "{\"groupId\":\"groupId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "{\"artifactId\":\"artifactId\",\"version\":\"version\",\"scopes\":[\"compile\"]}";
  }


  @Override
  protected String renderTypesResult() {
    return "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"],\"types\":[\"jar\",\"zip\"]}";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"],\"types\":[\"jar\"]}";
  }

  @Override
  protected String renderClassifiersResult() {
    return "{\"artifactId\":\"artifactId\",\"classifiers\":[\"classifier1\",\"classifier2\"],\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "{\"artifactId\":\"artifactId\",\"scopes\":[\"compile\"]}";
  }

  @Override
  protected String renderAllResult() {
    return "{\"groupId\":\"groupId\",\"artifactId\":\"artifactId\",\"version\":\"version\",\"classifiers\":[\"classifier1\",\"classifier2\"],\"scopes\":[\"test\"],\"types\":[\"jar\",\"tar.gz\",\"zip\"]}";
  }
}
