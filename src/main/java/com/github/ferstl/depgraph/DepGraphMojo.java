package com.github.ferstl.depgraph;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.artifact.filter.ScopeArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternExcludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

@Mojo(
    name = "aggregate",
    aggregator = true,
    defaultPhase = LifecyclePhase.NONE,
    inheritByDefault = false,
    requiresDependencyCollection = ResolutionScope.TEST,
    requiresDirectInvocation = true,
    threadSafe = true)
public class DepGraphMojo extends AbstractMojo {

  @Parameter(property = "scope")
  private String scope;

  @Parameter(property = "includes", defaultValue = "")
  private List<String> includes;

  @Parameter(property = "excludes", defaultValue = "")
  private List<String> excludes;

  @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/dependency-graph.dot")
  private File outputFile;

  @Component
  private MavenProject project;

  @Component( hint = "default" )
  private DependencyGraphBuilder dependencyGraphBuilder;

  @Override
  public void execute() throws MojoExecutionException {
    ArtifactFilter filter = createArtifactFilters();

    try {
      DotBuilder dotBuilder = new DotBuilder(NodeRenderers.VERSIONLESS_ID, NodeRenderers.ARTIFACT_ID);

      @SuppressWarnings("unchecked")
      List<MavenProject> collectedProjects = this.project.getCollectedProjects();
      buildModuleTree(collectedProjects, dotBuilder);

      for (MavenProject collectedProject : collectedProjects) {
        DependencyNode root = this.dependencyGraphBuilder.buildDependencyGraph(collectedProject, filter);

        DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder);
        root.accept(visitor);
      }

      writeDotFile(dotBuilder);
      System.err.println(dotBuilder);

    } catch (DependencyGraphBuilderException | IOException e) {
      throw new MojoExecutionException("Unable to create dependency graph.", e);
    }
  }

  private void writeDotFile(DotBuilder dotBuilder) throws IOException {
    Files.createParentDirs(this.outputFile);

    try(Writer writer = Files.newWriter(this.outputFile, Charsets.UTF_8)) {
      writer.write(dotBuilder.toString());
    }
  }

  private ArtifactFilter createArtifactFilters() {
    List<ArtifactFilter> filters = new ArrayList<>(3);

    if (this.scope != null) {
      filters.add(new ScopeArtifactFilter(this.scope));
    }

    if (!this.includes.isEmpty()) {
      filters.add(new StrictPatternIncludesArtifactFilter(this.includes));
    }

    if (!this.excludes.isEmpty()) {
      filters.add(new StrictPatternExcludesArtifactFilter(this.excludes));
    }

    return new AndArtifactFilter(filters);
  }

  private void buildModuleTree(Collection<MavenProject> collectedProjects, DotBuilder dotBuilder) {
    // FIXME apply filters here
    for (MavenProject collectedProject : collectedProjects) {
      MavenProject child = collectedProject;
      MavenProject parent = collectedProject.getParent();

      while (parent != null) {
        ArtifactNode parentNode = new ArtifactNode(parent.getArtifact());
        ArtifactNode childNode = new ArtifactNode(child.getArtifact());
        dotBuilder.addEdge(parentNode, childNode);

        child = parent;
        parent = parent.getParent();
      }
    }
  }
}
