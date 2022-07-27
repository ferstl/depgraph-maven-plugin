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
package com.github.ferstl.depgraph.dependency.puml;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PumlEdgeInfo {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private String begin;
  private String end;
  private String color;
  private String label;

  PumlEdgeInfo() {
  }

  @JsonCreator
  public PumlEdgeInfo(@JsonProperty("begin") String begin, @JsonProperty("end") String end,
      @JsonProperty("color") String color, @JsonProperty("label") String label) {
    this.begin = begin;
    this.end = end;
    this.color = color;
    this.label = label;
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public String getBegin() {
    return this.begin;
  }

  public String getEnd() {
    return this.end;
  }

  public String getColor() {
    return this.color;
  }

  public String getLabel() {
    return this.label;
  }

  PumlEdgeInfo withBegin(String begin) {
    this.begin = begin;
    return this;
  }

  PumlEdgeInfo withEnd(String end) {
    this.end = end;
    return this;
  }

  PumlEdgeInfo withColor(String color) {
    this.color = color;
    return this;
  }

  PumlEdgeInfo withLabel(String label) {
    this.label = label;
    return this;
  }

  public static PumlEdgeInfo parse(String serialized) {
    try {
      return OBJECT_MAPPER.readValue(serialized, PumlEdgeInfo.class);
    } catch (IOException e) {
      throw new RuntimeException("Cannot parse edge info from: " + serialized, e);
    }
  }

  @Override
  public String toString() {
    try {
      return OBJECT_MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Cannot serialize edge info. ", e);
    }
  }
}
