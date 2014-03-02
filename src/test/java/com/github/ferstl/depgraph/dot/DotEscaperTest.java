package com.github.ferstl.depgraph.dot;

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
    assertEquals("NoQuotingRequired", DotEscaper.escape("NoQuotingRequired"));
    assertEquals("\"Quoting Required\"", DotEscaper.escape("Quoting Required"));
    assertEquals("\"Quoting-Required\"", DotEscaper.escape("Quoting-Required"));
  }

  @Test
  public void quoteIfRequiredAlreadyQuoted() {
    assertEquals("\"Already quoted\"", DotEscaper.escape("\"Already quoted\""));
  }

}
