/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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

public abstract class AbstractGraphStyleConfigurer implements GraphStyleConfigurer {

  protected boolean showGroupId;
  protected boolean showArtifactId;
  protected boolean showVersionsOnNodes;
  protected boolean showTypes;
  protected boolean showClassifiers;
  protected boolean showVersionOnEdges;
  protected boolean showOptional;
  protected boolean showScope;

  @Override
  public final GraphStyleConfigurer showGroupIds(boolean showGroupId) {
    this.showGroupId = showGroupId;
    return this;
  }

  @Override
  public final GraphStyleConfigurer showArtifactIds(boolean showArtifactId) {
    this.showArtifactId = showArtifactId;
    return this;
  }

  @Override
  public final GraphStyleConfigurer showTypes(boolean showTypes) {
    this.showTypes = showTypes;
    return this;
  }

  @Override
  public final GraphStyleConfigurer showClassifiers(boolean showClassifiers) {
    this.showClassifiers = showClassifiers;
    return this;
  }

  @Override
  public final GraphStyleConfigurer showVersionsOnNodes(boolean showVersionsOnNodes) {
    this.showVersionsOnNodes = showVersionsOnNodes;
    return this;
  }

  @Override
  public final GraphStyleConfigurer showVersionsOnEdges(boolean showVersionOnEdges) {
    this.showVersionOnEdges = showVersionOnEdges;
    return this;
  }

  @Override
  public GraphStyleConfigurer showOptional(boolean optional) {
    this.showOptional = optional;
    return this;
  }

  @Override
  public GraphStyleConfigurer showScope(boolean showScope) {
    this.showScope = showScope;
    return this;
  }

  // Only relevant for the text graph. Don't do anything here.
  @Override
  public GraphStyleConfigurer repeatTransitiveDependencies(boolean repeatTransitiveDependencies) {
    return this;
  }
}
