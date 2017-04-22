package com.github.ferstl.depgraph.dependency;

import com.github.ferstl.depgraph.dependency.style.StyleConfiguration;

public class DotGraphStyleConfigurerTest extends AbstractGraphStyleConfigurerTest {

  @Override
  protected GraphStyleConfigurer createGraphStyleConfigurer() {
    return new DotGraphStyleConfigurer(new StyleConfiguration());
  }

  @Override
  protected String getNodeNameForGroupIdOnly(String groupId) {
    return "[label=<" + groupId + ">]";
  }

  @Override
  protected String getNodeNameForArtifactIdOnly(String artifactId) {
    return "[label=<" + artifactId + ">]";
  }

  @Override
  protected String getNodeNameForVersionOnly(String version) {
    return "[label=<" + version + ">]";
  }

  @Override
  protected String getEdgeNameForNonConflictingVersion(String version) {
    return "";
  }

  @Override
  protected String getEdgeNameForConflictingVersion(String conflictingVersion, boolean showVersion) {
    return showVersion ? "[label=\"" + conflictingVersion + "\"]" : "";
  }

  @Override
  protected String getNodeNameForAllAttributes(String groupId, String artifactId, String version) {
    return "[label=<" + groupId + "<br/>" + artifactId + "<br/>" + version + ">]";
  }

  @Override
  protected String getEmptyNodeName() {
    return "[label=\"\"]";
  }
}
