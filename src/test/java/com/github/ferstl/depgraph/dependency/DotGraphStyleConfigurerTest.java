/*
 * Copyright (c) 2014 - 2017 the original author or authors.
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
