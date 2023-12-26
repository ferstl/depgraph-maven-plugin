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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StyleKeyTest {

  private StyleKey groupId;
  private StyleKey artifactId;
  private StyleKey scope;
  private StyleKey type;
  private StyleKey version;
  private StyleKey classifier;
  private StyleKey optional;

  @BeforeEach
  void before() {
    this.groupId = StyleKey.fromString("group.id");
    this.artifactId = StyleKey.fromString(",artifactId");
    this.scope = StyleKey.fromString(",,scope");
    this.type = StyleKey.fromString(",,,type");
    this.version = StyleKey.fromString(",,,,version");
    this.classifier = StyleKey.fromString(",,,,,classifier");
    this.optional = StyleKey.fromString(",,,,,,true");
  }

  @Test
  void fromStringWithGroupIdOnly() {
    assertEquals("group.id,,,,,,", this.groupId.toString());
  }

  @Test
  void fromStringWithArtifactIdOnly() {
    assertEquals(",artifactId,,,,,", this.artifactId.toString());
  }

  @Test
  void fromStringWithScopeOnly() {
    assertEquals(",,scope,,,,", this.scope.toString());
  }

  @Test
  void fromStringWithTypeOnly() {
    assertEquals(",,,type,,,", this.type.toString());
  }

  @Test
  void fromStringWithVersionOnly() {
    assertEquals(",,,,version,,", this.version.toString());
  }

  @Test
  void fromStringWithClassifierOnly() {
    assertEquals(",,,,,classifier,", this.classifier.toString());
  }

  @Test
  void fromStringWithOptionalOnly() {
    assertEquals(",,,,,,true", this.optional.toString());
  }

  @Test
  void fromStringEmpty() {
    StyleKey key = StyleKey.fromString("");

    assertEquals(",,,,,,", key.toString());
  }

  @Test
  void fromStringTooManyParts() {
    assertThrows(
        IllegalArgumentException.class,
        () -> StyleKey.fromString("groupId,artifactId,scope,type,version,something,classifier,else"));
  }

  @Test
  void create() {
    StyleKey key = StyleKey.create("groupId", "artifactId", "scope", "type", "version", "classifier", true);

    assertEquals("groupId,artifactId,scope,type,version,classifier,true", key.toString());
  }

  @Test
  void createWithNullValues() {
    StyleKey key = StyleKey.create(null, null, null, null, null, null, null);

    assertEquals(",,,,,,", key.toString());
  }

  @Test
  void equalsAndHashCode() {
    StyleKey groupIdEqual = StyleKey.fromString("group.id");
    StyleKey groupIdDifferent = StyleKey.fromString("group.id2");

    assertEquals(this.groupId, groupIdEqual);
    assertEquals(groupIdEqual, this.groupId);
    assertNotEquals(this.groupId, groupIdDifferent);
    assertNotEquals(groupIdDifferent, this.groupId);
    assertEquals(this.artifactId, this.artifactId);
    assertNotEquals(this.version, "something else");


    assertEquals(this.groupId.hashCode(), groupIdEqual.hashCode());
    assertNotEquals(this.artifactId.hashCode(), this.scope.hashCode());
  }

  @Test
  void matchesForGroupId() {
    StyleKey wildcard = StyleKey.fromString("group.id*");

    assertTrue(this.groupId.matches(this.groupId));
    assertTrue(wildcard.matches(this.groupId));
  }

  @Test
  void matchesForArtifactId() {
    StyleKey wildcard = StyleKey.fromString(",artifactId*");

    assertTrue(this.artifactId.matches(this.artifactId));
    assertTrue(wildcard.matches(this.artifactId));
  }

  @Test
  void matchesForScope() {
    StyleKey unsupportedWildcard = StyleKey.fromString(",,scope*");

    assertTrue(this.scope.matches(this.scope));
    assertFalse(unsupportedWildcard.matches(this.scope));
  }

  @Test
  void matchesForType() {
    StyleKey unsupportedWildcard = StyleKey.fromString(",,,type*");

    assertTrue(this.type.matches(this.type));
    assertFalse(unsupportedWildcard.matches(this.type));
  }

  @Test
  void matchesForVersion() {
    StyleKey wildcard = StyleKey.fromString(",,,,version*");

    assertTrue(this.version.matches(this.version));
    assertTrue(wildcard.matches(this.version));
  }

  @Test
  void matchesForClassifier() {
    StyleKey wildcard = StyleKey.fromString(",,,,,classifier*");

    assertTrue(this.classifier.matches(this.classifier));
    assertTrue(wildcard.matches(this.classifier));
  }

  @Test
  void matchesForOptional() {
    StyleKey unsupportedWildcard = StyleKey.fromString(",,,,,,true*");

    assertTrue(this.optional.matches(this.optional));
    assertFalse(unsupportedWildcard.matches(this.optional));
  }

  @Test
  void matchesForWildcard() {
    StyleKey styleKey = StyleKey.fromString("groupId,artifactId,scope,type,version,classifier,optional");
    StyleKey wildcard = StyleKey.fromString("gr*Id,*tifa*Id,,,versi**on,*ssif*,");

    assertTrue(wildcard.matches(styleKey));
  }
}
