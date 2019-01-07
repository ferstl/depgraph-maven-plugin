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

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class FileSystemStyleResourceTest extends AbstractStyleResourceTest {

  public FileSystemStyleResourceTest() {
    super(new FileSystemStyleResource(Paths.get("src/main/resources/default-style.json")), new FileSystemStyleResource(Paths.get("src/main/resources/does-not-exist")));
  }

  @Test
  public void toStringTest() {
    Path path = Paths.get("src/main/resources/default-style.json");
    assertEquals("file:" + path.toString(), this.existingResource.toString());
  }
}
