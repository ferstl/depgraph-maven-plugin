package com.github.ferstl.depgraph.graph.style;

import com.github.ferstl.depgraph.dot.AttributeBuilder;

public interface GlobalConfigurer {

  void configureGlobally(AttributeBuilder builder);
}
