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

import java.io.IOException;
import java.io.InputStream;


public class ClasspathStyleResource implements StyleResource {

  private final String name;
  private final ClassLoader classLoader;

  public ClasspathStyleResource(String name, ClassLoader classLoader) {
    this.name = name;
    this.classLoader = classLoader;
  }

  @Override
  public boolean exists() {
    return this.classLoader.getResource(this.name) != null;
  }

  @Override
  public InputStream openStream() throws IOException {
    InputStream inputStream = this.classLoader.getResourceAsStream(this.name);
    if (inputStream == null) {
      throw new IOException("Resource " + this.name + " does not exist.");
    }

    return inputStream;
  }

  @Override
  public String toString() {
    return "classpath:" + this.name;
  }
}
