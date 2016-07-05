package com.github.ferstl.depgraph.graph.style;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;


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
  public void singleOrdering() {
    assertThat(this.groupId.compareTo(this.artifactId), greaterThan(0));
    assertThat(this.artifactId.compareTo(this.groupId), lessThan(0));
    assertThat(this.groupId.compareTo(this.groupId), equalTo(0));

    assertThat(this.artifactId.compareTo(this.scope), greaterThan(0));
    assertThat(this.scope.compareTo(this.artifactId), lessThan(0));
    assertThat(this.artifactId.compareTo(this.artifactId), equalTo(0));

    assertThat(this.scope.compareTo(this.type), greaterThan(0));
    assertThat(this.type.compareTo(this.scope), lessThan(0));
    assertThat(this.scope.compareTo(this.scope), equalTo(0));

    assertThat(this.type.compareTo(this.version), greaterThan(0));
    assertThat(this.version.compareTo(this.type), lessThan(0));
    assertThat(this.type.compareTo(this.type), equalTo(0));
  }

  @Test
  public void multiOrdering() {
    StyleKey full = StyleKey.fromString("groupId,artifactId,scope,type,version");
    StyleKey noVersion = StyleKey.fromString("groupId,artifactId,scope,type");

    assertThat(full.compareTo(this.groupId), greaterThan(0));
    assertThat(full.compareTo(this.artifactId), greaterThan(0));
    assertThat(full.compareTo(this.scope), greaterThan(0));
    assertThat(full.compareTo(this.type), greaterThan(0));
    assertThat(full.compareTo(this.version), greaterThan(0));
    assertThat(full.compareTo(noVersion), greaterThan(0));

    assertThat(this.groupId.compareTo(full), lessThan(0));
    assertThat(this.artifactId.compareTo(full), lessThan(0));
    assertThat(this.scope.compareTo(full), lessThan(0));
    assertThat(this.type.compareTo(full), lessThan(0));
    assertThat(this.version.compareTo(full), lessThan(0));
    assertThat(noVersion.compareTo(full), lessThan(0));

    assertThat(full.compareTo(full), equalTo(0));
  }

  @Test
  public void equalsAndHashCode() {
    StyleKey groupId2 = StyleKey.fromString("groupId2");

    assertEquals(this.groupId, groupId2);
    assertEquals(groupId2, this.groupId);
    assertEquals(this.artifactId, this.artifactId);
    assertNotEquals(this.version, "something else");


    assertEquals(this.groupId.hashCode(), groupId2.hashCode());
    assertNotEquals(this.artifactId.hashCode(), this.scope.hashCode());
  }
}
