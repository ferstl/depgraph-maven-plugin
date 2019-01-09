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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractStyleResourceTest {

  final StyleResource existingResource;
  final StyleResource nonExistingResource;

  AbstractStyleResourceTest(StyleResource existingResource, StyleResource nonExistingResource) {
    this.existingResource = existingResource;
    this.nonExistingResource = nonExistingResource;
  }

  @Test
  void existsOnExistingResource() {
    assertTrue(this.existingResource.exists());
  }

  @Test
  void openStreamOnExistingResource() {
    try (InputStream is = this.existingResource.openStream()) {
      assertNotNull(is);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @Test
  void existsOnNonExistingResource() {
    assertFalse(this.nonExistingResource.exists());
  }

  @Test
  void openStreamOnNonExistingResource() throws IOException {
    assertThrows(IOException.class, this.nonExistingResource::openStream);
  }
}
