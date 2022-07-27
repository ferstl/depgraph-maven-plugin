/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.dot.style;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Joiner;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.startsWith;

public final class StyleKey {

  private static final int NUM_ELEMENTS = 7;

  private final String groupId;
  private final String artifactId;
  private final String scope;
  private final String type;
  private final String version;
  private final String classifier;
  private final String optional;


  private StyleKey(String[] parts) {
    if (parts.length > NUM_ELEMENTS) {
      throw new IllegalArgumentException("Too many parts. Expecting '<groupId>:<artifactId>:<version>:<scope>:<type>:<classifier>:<true|false>'");
    }

    String[] expanded = new String[NUM_ELEMENTS];
    for (int i = 0; i < expanded.length; i++) {
      expanded[i] = "";
    }

    for (int i = 0; i < parts.length; i++) {
      expanded[i] = defaultIfEmpty(parts[i], "");
    }

    this.groupId = expanded[0];
    this.artifactId = expanded[1];
    this.scope = expanded[2];
    this.type = expanded[3];
    this.version = expanded[4];
    this.classifier = expanded[5];
    this.optional = expanded[6];
  }

  public static StyleKey fromString(String keyString) {
    String[] parts = keyString.split(",");
    return new StyleKey(parts);
  }

  public static StyleKey create(String groupId, String artifactId, String scope, String type, String version, String classifier, Boolean isOptional) {
    return new StyleKey(new String[]{groupId, artifactId, scope, type, version, classifier, isOptional != null ? isOptional.toString() : null});
  }

  public boolean matches(StyleKey other) {
    return (wildcardMatch(this.groupId, other.groupId))
        && (wildcardMatch(this.artifactId, other.artifactId))
        && (match(this.scope, other.scope))
        && (match(this.type, other.type))
        && (wildcardMatch(this.version, other.version))
        && (wildcardMatch(this.classifier, other.classifier))
        && (match(this.optional, other.optional));

  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof StyleKey)) {
      return false;
    }

    StyleKey other = (StyleKey) obj;

    return Objects.equals(this.groupId, other.groupId)
        && Objects.equals(this.artifactId, other.artifactId)
        && Objects.equals(this.scope, other.scope)
        && Objects.equals(this.type, other.type)
        && Objects.equals(this.version, other.version)
        && Objects.equals(this.classifier, other.classifier)
        && Objects.equals(this.optional, other.optional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.groupId, this.artifactId, this.scope, this.type, this.version, this.classifier, this.optional);
  }

  @Override
  public String toString() {
    return Joiner.on(",").join(this.groupId, this.artifactId, this.scope, this.type, this.version, this.classifier, this.optional);
  }

  private static boolean wildcardMatch(String value1, String value2) {
    if (StringUtils.endsWith(value1, "*")) {
      return startsWith(value2, value1.substring(0, value1.length() - 1));
    }

    return match(value1, value2);
  }

  private static boolean match(String value1, String value2) {
    return value1.isEmpty() || value1.equals(value2);
  }

}
