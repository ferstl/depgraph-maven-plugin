package com.github.ferstl.depgraph.dependency;

import java.util.Collection;
import org.apache.maven.project.MavenProject;

/**
 * Functional interface to supply sub projects for a parent project. This is only used
 * to avoid injecting Maven infrastructure into the graph factory.
 */
// TODO Remove this after Java8 migration
public interface SubProjectSupplier {

  Collection<MavenProject> getSubProjects(MavenProject parent);
}
