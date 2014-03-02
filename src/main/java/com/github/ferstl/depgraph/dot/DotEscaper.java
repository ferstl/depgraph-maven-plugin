package com.github.ferstl.depgraph.dot;

import java.util.regex.Pattern;


public final class DotEscaper {

  private static final String QUOTE_REPLACEMENT = "\\\\\"";
  private static final Pattern REQUIRE_QUOTING_PATTERN = Pattern.compile(".*[\\s\\p{Punct}].*", Pattern.DOTALL);
  private static final Pattern REPLACE_NEWLINE_PATTERN = Pattern.compile("(\\r\\n)|[\\r\\n]", Pattern.DOTALL);
  private static final Pattern REPLACE_QUOTE_PATTERN = Pattern.compile("\"");

  private String value;

  private DotEscaper(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static String escape(String value) {
    return new DotEscaper(value)
      .escapeNewLines()
      .escapeQuotes()
      .quoteIfRequired()
      .getValue();
  }

  private DotEscaper escapeNewLines() {
    this.value = REPLACE_NEWLINE_PATTERN.matcher(this.value).replaceAll("\\\\n");
    return this;
  }

  private DotEscaper escapeQuotes() {
    if (isQuoted()) {
      String valueToQuote = this.value.substring(1, this.value.length() - 1);
      this.value = "\"" + REPLACE_QUOTE_PATTERN.matcher(valueToQuote).replaceAll(QUOTE_REPLACEMENT) + "\"";
    } else {
      this.value = REPLACE_QUOTE_PATTERN.matcher(this.value).replaceAll(QUOTE_REPLACEMENT);
    }

    return this;
  }

  private DotEscaper quoteIfRequired() {
    if (requiresQuoting()) {
      this.value = "\"" + this.value + "\"";
    }

    return this;
  }

  private boolean requiresQuoting() {
    return !isQuoted() && REQUIRE_QUOTING_PATTERN.matcher(this.value).matches();
  }

  private boolean isQuoted() {
    return this.value.startsWith("\"") && this.value.endsWith("\"");
  }
}
