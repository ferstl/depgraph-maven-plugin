package com.github.ferstl.depgraph;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.artifact.filter.ScopeArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternExcludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.github.ferstl.depgraph.dot.DotBuilder;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;


abstract class AbstractDepGraphMojo extends AbstractMojo {

  private static final String DOT_EXTENSION = ".dot";
  private static final String OUTPUT_DOT_FILE_NAME = "dependency-graph" + DOT_EXTENSION;

  @Parameter(property = "scope")
  private String scope;

  @Parameter(property = "includes", defaultValue = "")
  private List<String> includes;

  @Parameter(property = "excludes", defaultValue = "")
  private List<String> excludes;

  @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/" + OUTPUT_DOT_FILE_NAME)
  private File outputFile;

  @Parameter(property = "createImage", defaultValue = "false")
  private boolean createImage;

  @Parameter(property = "imageFormat", defaultValue = "png")
  private String imageFormat;

  @Component
  private MavenProject project;

  @Component( hint = "default" )
  private DependencyGraphBuilder dependencyGraphBuilder;

  @Override
  public void execute() throws MojoExecutionException {
    ArtifactFilter filter = createArtifactFilters();

    try {
      DotBuilder dotBuilder = createDotBuilder();

      @SuppressWarnings("unchecked")
      List<MavenProject> collectedProjects = this.project.getCollectedProjects();
      buildModuleTree(collectedProjects, dotBuilder);

      for (MavenProject collectedProject : collectedProjects) {
        DependencyNode root = this.dependencyGraphBuilder.buildDependencyGraph(collectedProject, filter);

        DotBuildingVisitor visitor = new DotBuildingVisitor(dotBuilder);
        root.accept(visitor);
      }

      writeDotFile(dotBuilder);

      if (this.createImage) {
        generateGraphImage();
      }

    } catch (DependencyGraphBuilderException | IOException e) {
      throw new MojoExecutionException("Unable to create dependency graph.", e);
    }
  }

  protected abstract DotBuilder createDotBuilder();

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

        // Stop if we reached this project!
        if (parent.equals(this.project)) {
          break;
        }

        child = parent;
        parent = parent.getParent();
      }
    }
  }

  private void writeDotFile(DotBuilder dotBuilder) throws IOException {
    Files.createParentDirs(this.outputFile);

    try(Writer writer = Files.newWriter(this.outputFile, Charsets.UTF_8)) {
      writer.write(dotBuilder.toString());
    }
  }

  private void generateGraphImage() throws IOException {
    String graphFileName = createGraphFileName();

    Path graphFile = Paths.get(this.outputFile.toPath().getParent().toString(), graphFileName);
    List<String> commandLine = Arrays.asList(
        "dot",
        "-T" + this.imageFormat,
        "-o\"" + graphFile.toAbsolutePath() + "\"",
        "\"" + this.outputFile.getAbsolutePath() + "\"");

    getLog().info("Running Graphviz: " + Joiner.on(" ").join(commandLine));

    ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
    Process process = processBuilder.start();
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      process.destroy();
    }

    getLog().info("Graph file created on " + graphFile.toAbsolutePath());
  }

  private String createGraphFileName() {
    String dotFileName = this.outputFile.getName();

    String graphFileName;
    if (dotFileName.endsWith(DOT_EXTENSION)) {
      graphFileName = dotFileName.substring(0, dotFileName.lastIndexOf(".")) + "." + this.imageFormat;
    } else {
      graphFileName = dotFileName + this.imageFormat;
    }
    return graphFileName;
  }
}
