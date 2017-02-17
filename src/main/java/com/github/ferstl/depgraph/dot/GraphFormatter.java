package com.github.ferstl.depgraph.dot;

import java.util.Collection;

/**
 * Created by sferstl on 17.02.2017.
 */
public interface GraphFormatter {

  String format(String graphName, Collection<Node<?>> nodes, Collection<Edge> edges);
}
