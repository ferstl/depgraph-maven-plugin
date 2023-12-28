/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
package com.github.ferstl.depgraph.dependency.dot.style;

import java.io.IOException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.github.ferstl.depgraph.dependency.NodeResolution;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NodeResolutionSerializationTest {

  private final NodeResolutionDeserializer deserializer;
  private final NodeResolutionSerializer serializer;

  NodeResolutionSerializationTest() {
    this.serializer = new NodeResolutionSerializer();
    this.deserializer = new NodeResolutionDeserializer();
  }

  @ParameterizedTest
  @EnumSource(NodeResolution.class)
  void serializationAndDeserialization(NodeResolution resolution) throws IOException {
    String serialized = serializeAndVerify(resolution);
    deserializeAndVerify(resolution, serialized);
  }

  private String serializeAndVerify(NodeResolution resolution) throws IOException {
    JsonGenerator generator = mock(JsonGenerator.class);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    this.serializer.serialize(resolution, generator, null);
    verify(generator).writeFieldName(captor.capture());
    return captor.getValue();
  }

  private void deserializeAndVerify(NodeResolution resolution, String serialized) throws IOException {
    JsonParser parser = mock(JsonParser.class);
    when(parser.getText()).thenReturn(serialized);

    NodeResolution deserialized = this.deserializer.deserialize(parser, null);
    assertEquals(resolution, deserialized);
  }
}
