package com.github.ferstl.depgraph.dot;


public interface NodeAttributeRenderer<T> {

  String createNodeAttributes(T node);
}
