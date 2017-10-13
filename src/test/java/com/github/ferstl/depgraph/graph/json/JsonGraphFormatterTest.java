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
package com.github.ferstl.depgraph.graph.json;

import org.junit.Before;
import org.junit.Test;
import com.github.ferstl.depgraph.graph.Edge;
import com.github.ferstl.depgraph.graph.Node;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class JsonGraphFormatterTest {

  private JsonGraphFormatter formatter;

  @Before
  public void before() {
    this.formatter = new JsonGraphFormatter();
  }

  @Test
  public void format() {
    // arrange
    Node<?> node1 = new Node<>("id1", "{\"groupId\": \"com.github.ferstl\", \"artifactId\": \"id1\"}", new Object());
    Node<?> node2 = new Node<>("id2", "{}", new Object());
    Node<?> node3 = new Node<>("id3", "{\"artifactId\": \"id3\", \"version\": \"1.0.0\"}", new Object());

    Edge edge1 = new Edge("id1", "id2", "{\"resolution\": \"OMMITTED_FOR_CONFLICT\"}");
    Edge edge2 = new Edge("id1", "id2", "{}");

    // act
    String result = this.formatter.format("graphName", asList(node1, node2, node3), asList(edge1, edge2));

    // assert
    String expected = "{\n"
        + "  \"graphName\" : \"graphName\",\n"
        + "  \"artifacts\" : [ {\n"
        + "    \"id\" : \"id1\",\n"
        + "    \"numericId\" : 1,\n"
        + "    \"groupId\" : \"com.github.ferstl\",\n"
        + "    \"artifactId\" : \"id1\"\n"
        + "  }, {\n"
        + "    \"id\" : \"id2\",\n"
        + "    \"numericId\" : 2\n"
        + "  }, {\n"
        + "    \"id\" : \"id3\",\n"
        + "    \"numericId\" : 3,\n"
        + "    \"artifactId\" : \"id3\",\n"
        + "    \"version\" : \"1.0.0\"\n"
        + "  } ],\n"
        + "  \"dependencies\" : [ {\n"
        + "    \"from\" : \"id1\",\n"
        + "    \"to\" : \"id2\",\n"
        + "    \"numericFrom\" : 0,\n"
        + "    \"numericTo\" : 1,\n"
        + "    \"resolution\" : \"OMMITTED_FOR_CONFLICT\"\n"
        + "  }, {\n"
        + "    \"from\" : \"id1\",\n"
        + "    \"to\" : \"id2\",\n"
        + "    \"numericFrom\" : 0,\n"
        + "    \"numericTo\" : 1\n"
        + "  } ]\n"
        + "}";

    assertEquals(expected, result);
  }
}
