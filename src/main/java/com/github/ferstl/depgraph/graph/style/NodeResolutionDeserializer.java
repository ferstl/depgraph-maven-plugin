package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.ferstl.depgraph.graph.NodeResolution;


public class NodeResolutionDeserializer extends JsonDeserializer<NodeResolution> {

  @Override
  public NodeResolution deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String name = p.getText().replace('-', '_').toUpperCase();
    return NodeResolution.valueOf(name);
  }

}
