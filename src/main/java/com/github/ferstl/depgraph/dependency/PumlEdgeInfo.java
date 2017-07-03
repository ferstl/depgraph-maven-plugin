package com.github.ferstl.depgraph.dependency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author gushakov
 */
public class PumlEdgeInfo {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String begin;

    private String end;

    private String color;

    private String label;

    PumlEdgeInfo(){}

    @JsonCreator
    public PumlEdgeInfo(@JsonProperty("begin") String begin, @JsonProperty("end") String end,
                        @JsonProperty("color") String color, @JsonProperty("label") String label) {
        this.begin = begin;
        this.end = end;
        this.color = color;
        this.label = label;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    PumlEdgeInfo withBegin(String begin){
        this.begin = begin;
        return this;
    }

    PumlEdgeInfo withEnd(String end){
        this.end = end;
        return this;
    }

    PumlEdgeInfo withColor(String color){
        this.color = color;
        return this;
    }

    PumlEdgeInfo withLabel(String label){
        this.label = label;
        return this;
    }

    public static PumlEdgeInfo parse(String serialized){
        try {
            return objectMapper.readValue(serialized, PumlEdgeInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse edge info from: " + serialized, e);
        }
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize edge info. ", e);
        }
    }
}
