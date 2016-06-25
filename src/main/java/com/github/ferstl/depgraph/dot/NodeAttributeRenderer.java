package com.github.ferstl.depgraph.dot;


public interface NodeAttributeRenderer<T> {

  AttributeBuilder createNodeAttributes(T node);
}
