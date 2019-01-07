/*
 * Copyright (c) 2014 - 2019 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.gml;

import com.github.ferstl.depgraph.dependency.AbstractGraphStyleConfigurerTest;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;

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
        + "targetArrow \"standard\"\n"
        + "fill \"#FF0000\"\n"
        + "]\n"
        + "LabelGraphics\n"
        + "[\n"
        + "color \"#FF0000\"\n"
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
