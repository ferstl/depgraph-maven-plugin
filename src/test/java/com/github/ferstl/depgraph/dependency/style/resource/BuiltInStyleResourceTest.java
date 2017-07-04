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
package com.github.ferstl.depgraph.dependency.style.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.github.ferstl.depgraph.dependency.style.StyleConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BuiltInStyleResourceTest {

  private final BuiltInStyleResource resource;

  public BuiltInStyleResourceTest(BuiltInStyleResource resource) {
    this.resource = resource;
  }

  @Parameters(name = "{0}")
  public static BuiltInStyleResource[] resources() {
    return BuiltInStyleResource.values();
  }

  @Test
  public void resourcesExistAndCanBeLoaded() {
    ClasspathStyleResource styleResource = this.resource.createStyleResource(getClass().getClassLoader());

    assertTrue("Style configuration " + this.resource + " does not exist.", styleResource.exists());
    StyleConfiguration.load(styleResource);
  }

}
