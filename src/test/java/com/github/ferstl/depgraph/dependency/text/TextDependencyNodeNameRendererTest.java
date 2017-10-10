package com.github.ferstl.depgraph.dependency.text;

import com.github.ferstl.depgraph.dependency.AbstractDependencyNodeNameRendererTest;
import com.github.ferstl.depgraph.dependency.DependencyNode;
import com.github.ferstl.depgraph.graph.NodeRenderer;

public class TextDependencyNodeNameRendererTest extends AbstractDependencyNodeNameRendererTest {

  @Override
  protected NodeRenderer<DependencyNode> createNodeNameRenderer(boolean showGroupId, boolean showArtifactId, boolean showTypes, boolean showClassifiers, boolean showVersion) {
    return new TextDependencyNodeNameRenderer(showGroupId, showArtifactId, showTypes, showClassifiers, showVersion);
  }

  @Override
  protected String renderNothingResult() {
    return "compile";
  }

  @Override
  protected String renderGroupIdResult() {
    return "groupId:compile";
  }

  @Override
  protected String renderArtifactIdResult() {
    return "artifactId:compile";
  }

  @Override
  protected String renderVersionResult() {
    return "version:compile";
  }

  @Override
  protected String renderGroupIdArtifactIdVersionResult() {
    return "groupId:artifactId:version:compile";
  }

  @Override
  protected String renderGroupIdArtifactIdResult() {
    return "groupId:artifactId:compile";
  }

  @Override
  protected String renderGroupIdVersionResult() {
    return "groupId:version:compile";
  }

  @Override
  protected String renderArtifactIdVersionResult() {
    return "artifactId:version:compile";
  }

  @Override
  protected String renderTypesResult() {
    return "artifactId:jar/zip:compile";
  }

  @Override
  protected String renderJarTypeOnlyResult() {
    return "artifactId:jar:compile";
  }

  @Override
  protected String renderClassifiersResult() {
    return "artifactId:classifier1/classifier2:compile";
  }

  @Override
  protected String renderEmptyClassifierResult() {
    return "artifactId::compile";
  }

  @Override
  protected String renderAllResult() {
    return "groupId:artifactId:version:jar/tar.gz/zip:classifier1/classifier2:test";
  }
}
