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
package com.github.ferstl.depgraph.graph.dot;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for {@link DotEscaper}.
 */
public class DotEscaperTest {


  @Test
  public void escapeNewlinesBackslashN() {
    assertEquals("\"backslashN\\n\"", DotEscaper.escape("backslashN\n"));
  }

  @Test
  public void escapeNewlinesBackslashR() {
    assertEquals("\"backslashR\\n\"", DotEscaper.escape("backslashR\r"));
  }

  @Test
  public void escapeNewlinesBackslashRN() {
    assertEquals("\"backslashRN\\n\"", DotEscaper.escape("backslashRN\r\n"));
  }

  @Test
  public void escapeQuotes() throws Exception {
    assertEquals("\"there are \\\"quotes\\\" in the middle.\"", DotEscaper.escape("there are \"quotes\" in the middle."));
  }

  @Test
  public void escapeQuotesAlreadyQuoted() throws Exception {
    assertEquals("\"there are \\\"quotes\\\" in the middle.\"", DotEscaper.escape("\"there are \"quotes\" in the middle.\""));
  }

  @Test
  public void quoteIfRequired() {
    assertEquals("\"QuotingRequired\"", DotEscaper.escape("QuotingRequired"));
  }

  @Test
  public void quoteIfRequiredAlreadyQuoted() {
    assertEquals("\"Already quoted\"", DotEscaper.escape("\"Already quoted\""));
  }

}
