package com.github.ferstl.depgraph.graph.style.resource;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.style.resource.StyleResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractStyleResourceTest {

  private final StyleResource existingResource;
  private final StyleResource nonExistingResource;

  public AbstractStyleResourceTest(StyleResource existingResource, StyleResource nonExistingResource) {
    this.existingResource = existingResource;
    this.nonExistingResource = nonExistingResource;
  }

  @Test
  public void existsOnExistingResource() {
    assertTrue(this.existingResource.exists());
  }

  @Test
  public void openStreamOnExistingResource() {
    try (InputStream is = this.existingResource.openStream()) {
      assertNotNull(is);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @Test
  public void existsOnNonExistingResource() {
    assertFalse(this.nonExistingResource.exists());
  }

  @Test(expected = IOException.class)
  public void openStreamOnNonExistingResource() throws IOException {
    try (InputStream is = this.nonExistingResource.openStream()) {
      fail("There should not be a stream on an inexistent resource.");
    }
  }
}
