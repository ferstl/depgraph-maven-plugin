package com.github.ferstl.depgraph;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.ferstl.depgraph.dot.DotBuilder;

@Mojo(
    name = "by-groupid",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class GraphByGroupIdMojo extends AbstractDepGraphMojo {

  @Override
  protected DotBuilder createDotBuilder() {
    return new DotBuilder(NodeRenderers.SCOPED_GROUP_ID, NodeRenderers.GROUP_ID_LABEL);
  }

}
