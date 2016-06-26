package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.github.ferstl.depgraph.graph.NodeResolution;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class NodeResolutionSerializationTest {

  private final NodeResolution resolution;
  private final NodeResolutionDeserializer deserializer;
  private final NodeResolutionSerializer serializer;

  public NodeResolutionSerializationTest(NodeResolution resolution) {
    this.resolution = resolution;
    this.serializer = new NodeResolutionSerializer();
    this.deserializer = new NodeResolutionDeserializer();
  }

  @Parameters(name = "{0}")
  public static NodeResolution[] getResolutions() {
    return NodeResolution.values();
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    String serialized = serializeAndVerify();
    deserializeAndVerify(serialized);
  }

  private String serializeAndVerify() throws IOException {
    JsonGenerator generator = mock(JsonGenerator.class);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    this.serializer.serialize(this.resolution, generator, null);
    verify(generator).writeFieldName(captor.capture());
    return captor.getValue();
  }

  private void deserializeAndVerify(String serialized) throws IOException {
    JsonParser parser = mock(JsonParser.class);
    when(parser.getText()).thenReturn(serialized);

    NodeResolution deserialized = this.deserializer.deserialize(parser, null);
    assertEquals(this.resolution, deserialized);
  }
}
