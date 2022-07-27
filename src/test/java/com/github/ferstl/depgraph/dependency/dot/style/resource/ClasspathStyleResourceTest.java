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
package com.github.ferstl.depgraph.dependency.dot.style.resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClasspathStyleResourceTest extends AbstractStyleResourceTest {

  ClasspathStyleResourceTest() {
    super(
        new ClasspathStyleResource("default-style.json", ClasspathStyleResourceTest.class.getClassLoader()),
        new ClasspathStyleResource("does-not-exist", ClasspathStyleResourceTest.class.getClassLoader()));
  }

  @Test
  void toStringTest() {
    assertEquals("classpath:default-style.json", this.existingResource.toString());
  }
}
