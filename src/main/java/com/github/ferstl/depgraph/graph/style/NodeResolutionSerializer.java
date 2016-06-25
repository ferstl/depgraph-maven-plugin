package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.ferstl.depgraph.graph.NodeResolution;

class NodeResolutionSerializer extends JsonSerializer<NodeResolution> {

  @Override
  public void serialize(NodeResolution value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeFieldName(value.name().toLowerCase().replace('_', '-'));
  }

}
