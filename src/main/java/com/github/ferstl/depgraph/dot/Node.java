package com.github.ferstl.depgraph.dot;

import org.apache.maven.artifact.Artifact;

import com.github.ferstl.depgraph.NodeResolution;


public interface Node {

  Artifact getArtifact();

  NodeResolution getResolution();
}
