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
package com.github.ferstl.depgraph.dependency;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gushakov
 */
public class PumlNodeInfo {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private String component;
  private String label;
  private String stereotype;

  PumlNodeInfo() {
  }

  @JsonCreator
  public PumlNodeInfo(@JsonProperty("component") String component,
      @JsonProperty("label") String label,
      @JsonProperty("stereotype") String stereotype) {
    this.component = component;
    this.label = label;
    this.stereotype = stereotype;
  }

  public String getComponent() {
    return this.component;
  }

  public String getLabel() {
    return this.label;
  }

  public String getStereotype() {
    return this.stereotype;
  }

  public PumlNodeInfo withComponent(String component) {
    this.component = component;
    return this;
  }

  public PumlNodeInfo withLabel(String label) {
    this.label = label;
    return this;
  }

  public PumlNodeInfo withStereotype(String stereotype) {
    this.stereotype = stereotype;
    return this;
  }

  public static PumlNodeInfo parse(String serialized) {
    try {
      return OBJECT_MAPPER.readValue(serialized, PumlNodeInfo.class);
    } catch (IOException e) {
      throw new RuntimeException("Cannot parse PUML node info from: " + serialized, e);
    }
  }

  @Override
  public String toString() {
    try {
      return OBJECT_MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Cannot serialize PUML node info", e);
    }
  }
}
