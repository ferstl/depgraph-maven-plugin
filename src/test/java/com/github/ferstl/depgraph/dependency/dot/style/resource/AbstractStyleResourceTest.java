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
package com.github.ferstl.depgraph.dependency.dot.style.resource;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractStyleResourceTest {

  final StyleResource existingResource;
  final StyleResource nonExistingResource;

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
