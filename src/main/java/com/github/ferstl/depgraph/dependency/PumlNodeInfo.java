package com.github.ferstl.depgraph.dependency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author gushakov
 */
public class PumlNodeInfo {

    private static final ObjectMapper mapper = new ObjectMapper();

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
        return component;
    }

    public String getLabel() {
        return label;
    }

    public String getStereotype() {
        return stereotype;
    }

    public PumlNodeInfo withComponent(String component){
        this.component = component;
        return this;
    }

    public PumlNodeInfo withLabel(String label){
        this.label = label;
        return this;
    }

    public PumlNodeInfo withStereotype(String stereotype){
        this.stereotype = stereotype;
        return this;
    }

    public static PumlNodeInfo parse(String serialized){
        try {
            return mapper.readValue(serialized, PumlNodeInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse PUML node info from: " + serialized, e);
        }
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize PUML node info", e);
        }
    }
}
