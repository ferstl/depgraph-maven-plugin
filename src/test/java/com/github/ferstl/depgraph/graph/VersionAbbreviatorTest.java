package com.github.ferstl.depgraph.graph;

import org.junit.Test;

import static com.github.ferstl.depgraph.graph.VersionAbbreviator.abbreviateVersion;
import static org.junit.Assert.assertEquals;

public class VersionAbbreviatorTest {

  @Test
  public void abbreviateRegularVersion() {
    assertEquals("1.0.0", abbreviateVersion("1.0.0"));
  }

  @Test
  public void abbreviateSnapshotVersion() {
    assertEquals("1.0.0-S.", abbreviateVersion("1.0.0-SNAPSHOT"));
  }
}
