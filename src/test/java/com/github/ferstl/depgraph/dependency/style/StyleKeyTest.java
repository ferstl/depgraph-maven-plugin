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
package com.github.ferstl.depgraph.dependency.style;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class StyleKeyTest {

  private StyleKey groupId;
  private StyleKey artifactId;
  private StyleKey scope;
  private StyleKey type;
  private StyleKey version;

  @Before
  public void before() {
    this.groupId = StyleKey.fromString("group.id");
    this.artifactId = StyleKey.fromString(",artifactId");
    this.scope = StyleKey.fromString(",,scope");
    this.type = StyleKey.fromString(",,,type");
    this.version = StyleKey.fromString(",,,,version");
  }

  @Test
  public void fromStringWithGroupIdOnly() {
    assertEquals("group.id,,,,", this.groupId.toString());
  }

  @Test
  public void fromStringWithArtifactIdOnly() {
    assertEquals(",artifactId,,,", this.artifactId.toString());
  }

  @Test
  public void fromStringWithScopeOnly() {
    assertEquals(",,scope,,", this.scope.toString());
  }

  @Test
  public void fromStringWithTypeOnly() {
    assertEquals(",,,type,", this.type.toString());
  }

  @Test
  public void fromStringWithVersionOnly() {
    assertEquals(",,,,version", this.version.toString());
  }

  @Test
  public void fromStringEmpty() {
    StyleKey key = StyleKey.fromString("");

    assertEquals(",,,,", key.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromStringTooManyParts() {
    StyleKey.fromString("groupId,artifactId,scope,type,version,something,else");
  }

  @Test
  public void create() {
    StyleKey key = StyleKey.create("groupId", "artifactId", "scope", "type", "version");

    assertEquals("groupId,artifactId,scope,type,version", key.toString());
  }

  @Test
  public void equalsAndHashCode() {
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
  public void matchesForGroupId() {
    StyleKey wildcard = StyleKey.fromString("group.id*");

    assertTrue(this.groupId.matches(this.groupId));
    assertTrue(wildcard.matches(this.groupId));
  }

  @Test
  public void matchesForArtifactId() {
    StyleKey wildcard = StyleKey.fromString(",artifactId*");

    assertTrue(this.artifactId.matches(this.artifactId));
    assertTrue(wildcard.matches(this.artifactId));
  }

  @Test
  public void matchesForScope() {
    StyleKey unsupportedWildcard = StyleKey.fromString(",,scope*");

    assertTrue(this.scope.matches(this.scope));
    assertFalse(unsupportedWildcard.matches(this.scope));
  }

  @Test
  public void matchesForType() {
    StyleKey unsupportedWildcard = StyleKey.fromString(",,,type*");

    assertTrue(this.type.matches(this.type));
    assertFalse(unsupportedWildcard.matches(this.type));
  }

  @Test
  public void matchesForVersion() {
    StyleKey wildcard = StyleKey.fromString(",,,,version*");

    assertTrue(this.version.matches(this.version));
    assertTrue(wildcard.matches(this.version));
  }
}
