/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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
package com.github.ferstl.depgraph.graph.style;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Joiner;

public final class StyleKey {

  private static final int NUM_ELEMENTS = 5;

  String groupId;
  String artifactId;
  String scope;
  String type;
  String version;


  private StyleKey(String[] parts) {
    if (parts.length > NUM_ELEMENTS) {
      throw new IllegalArgumentException("Too many parts. Expecting '<groupId>:<artifactId>:<version>:<scope>:<type>'");
    }

    String[] expanded = new String[NUM_ELEMENTS];
    for (int i = 0; i < parts.length; i++) {
      expanded[i] = StringUtils.defaultIfEmpty(parts[i], null);
    }

    this.groupId = expanded[0];
    this.artifactId = expanded[1];
    this.scope = expanded[2];
    this.type = expanded[3];
    this.version = expanded[4];
  }

  public static StyleKey fromString(String keyString) {
    String[] parts = keyString.split(",");
    return new StyleKey(parts);
  }

  public static StyleKey create(String groupId, String artifactId, String scope, String type, String version) {
    return new StyleKey(new String[]{groupId, artifactId, scope, type, version});
  }

  public boolean matches(StyleKey other) {
    return (this.groupId == null || wildcardMatch(this.groupId, other.groupId))
        && (this.artifactId == null || wildcardMatch(this.artifactId, other.artifactId))
        && (this.scope == null || match(this.scope, other.scope))
        && (this.type == null || match(this.type, other.type))
        && (this.version == null || wildcardMatch(this.version, other.version));

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
        && Objects.equals(this.version, other.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.groupId, this.artifactId, this.scope, this.type, this.version);
  }

  @Override
  public String toString() {
    return Joiner.on(",").useForNull("").join(this.groupId, this.artifactId, this.scope, this.type, this.version);
  }

  private static boolean wildcardMatch(String value1, String value2) {
    if (StringUtils.endsWith(value1, "*")) {
      return StringUtils.startsWith(value2, value1.substring(0, value1.length() - 1));
    }

    return match(value1, value2);
  }

  private static boolean match(String value1, String value2) {
    return StringUtils.equals(value1, value2);
  }

}
