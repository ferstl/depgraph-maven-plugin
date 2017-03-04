package com.github.ferstl.depgraph.dependency;

public class GmlGraphStyleConfigurerTest extends AbstractGraphStyleConfigurerTest {

  @Override
  protected GraphStyleConfigurer createGraphStyleConfigurer() {
    return new GmlGraphStyleConfigurer();
  }

  @Override
  protected String getNodeNameForGroupIdOnly(String groupId) {
    return groupId;
  }

  @Override
  protected String getNodeNameForArtifactIdOnly(String artifactId) {
    return artifactId;
  }

  @Override
  protected String getNodeNameForVersionOnly(String version) {
    return version;
  }

  @Override
  protected String getEdgeNameForNonConflictingVersion(String version) {
    return "";
  }

  @Override
  protected String getEdgeNameForConflictingVersion(String conflictingVersion) {
    return conflictingVersion;
  }

  @Override
  protected String getNodeNameForAllAttributes(String groupId, String artifactId, String version) {
    return groupId + "\n" + artifactId + "\n" + version;
  }

  @Override
  protected String getEmptyNodeName() {
    return "";
  }
}
