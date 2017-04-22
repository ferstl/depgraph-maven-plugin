package com.github.ferstl.depgraph.dependency;

public class GmlGraphStyleConfigurerTest extends AbstractGraphStyleConfigurerTest {

  @Override
  protected GraphStyleConfigurer createGraphStyleConfigurer() {
    return new GmlGraphStyleConfigurer();
  }

  @Override
  protected String getNodeNameForGroupIdOnly(String groupId) {
    return "label \"" + groupId + "\"";
  }

  @Override
  protected String getNodeNameForArtifactIdOnly(String artifactId) {
    return "label \"" + artifactId + "\"";
  }

  @Override
  protected String getNodeNameForVersionOnly(String version) {
    return "label \"" + version + "\"";
  }

  @Override
  protected String getEdgeNameForNonConflictingVersion(String version) {
    return "";
  }

  @Override
  protected String getEdgeNameForConflictingVersion(String conflictingVersion, boolean showVersion) {
    String edgeName = "\ngraphics\n"
        + "[\n"
        + "style \"dashed\"\n"
        + "fill \"#FF0000\"\n"
        + "]";

    if (showVersion) {
      edgeName = "label \"" + conflictingVersion + "\"" + edgeName;
    }

    return edgeName;
  }

  @Override
  protected String getNodeNameForAllAttributes(String groupId, String artifactId, String version) {
    return "label \"" + groupId + "\n" + artifactId + "\n" + version + "\"";
  }

  @Override
  protected String getEmptyNodeName() {
    return "";
  }
}
