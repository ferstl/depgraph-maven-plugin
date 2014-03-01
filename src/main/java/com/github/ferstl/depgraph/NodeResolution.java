package com.github.ferstl.depgraph;


public enum NodeResolution {

  INCLUDED,
  OMITTED_FOR_DUPLICATE,
  OMMITTED_FOR_CONFLICT,
  OMMITTED_FOR_CYCLE;
}
