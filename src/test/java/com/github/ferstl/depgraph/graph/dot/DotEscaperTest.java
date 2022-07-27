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
package com.github.ferstl.depgraph.graph.dot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit tests for {@link DotEscaper}.
 */
class DotEscaperTest {


  @Test
  void escapeNewlinesBackslashN() {
    assertEquals("\"backslashN\\n\"", DotEscaper.escape("backslashN\n"));
  }

  @Test
  void escapeNewlinesBackslashR() {
    assertEquals("\"backslashR\\n\"", DotEscaper.escape("backslashR\r"));
  }

  @Test
  void escapeNewlinesBackslashRN() {
    assertEquals("\"backslashRN\\n\"", DotEscaper.escape("backslashRN\r\n"));
  }

  @Test
  void escapeQuotes() {
    assertEquals("\"there are \\\"quotes\\\" in the middle.\"", DotEscaper.escape("there are \"quotes\" in the middle."));
  }

  @Test
  void escapeQuotesAlreadyQuoted() {
    assertEquals("\"there are \\\"quotes\\\" in the middle.\"", DotEscaper.escape("\"there are \"quotes\" in the middle.\""));
  }

  @Test
  void quoteIfRequired() {
    assertEquals("\"QuotingRequired\"", DotEscaper.escape("QuotingRequired"));
  }

  @Test
  void quoteIfRequiredAlreadyQuoted() {
    assertEquals("\"Already quoted\"", DotEscaper.escape("\"Already quoted\""));
  }

}
