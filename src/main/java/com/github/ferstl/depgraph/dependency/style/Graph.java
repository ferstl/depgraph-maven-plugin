package com.github.ferstl.depgraph.dependency.style;

import org.apache.commons.lang3.StringUtils;
import com.github.ferstl.depgraph.graph.dot.DotAttributeBuilder;

public class Graph {

  private String rankdir;

  DotAttributeBuilder createAttributes() {
    return new DotAttributeBuilder().rankdir(this.rankdir);
  }

  void merge(Graph other) {
    this.rankdir = StringUtils.defaultIfBlank(other.rankdir, this.rankdir);
  }
}
