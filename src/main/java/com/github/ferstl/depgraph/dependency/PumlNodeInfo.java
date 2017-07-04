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
