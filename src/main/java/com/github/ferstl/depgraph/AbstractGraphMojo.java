/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ferstl.depgraph;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.repository.ArtifactRepository;
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
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

/**
 * Abstract mojo to create all possible kinds of graphs in the dot format. Graphs are created with instances of the
 * {@link GraphFactory} interface. This class defines an abstract method to create such factories. In case Graphviz is
 * install on the system where this plugin is executed, it is also possible to run the dot program and create images out
 * of the generated dot files. Besides that, this class allows the configuration of several basic mojo parameters, such
 * as includes, excludes, etc.
 */
abstract class AbstractGraphMojo extends AbstractMojo {

  private static final String DOT_EXTENSION = ".dot";
  private static final String OUTPUT_DOT_FILE_NAME = "dependency-graph" + DOT_EXTENSION;

  /**
   * The scope of the artifacts that should be included in the graph.
   *
   * @since 1.0.0
   */
  @Parameter(property = "scope")
  private String scope;

  /**
   * Comma-separated list of artifacts to be included in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "includes", defaultValue = "")
  private List<String> includes;

  /**
   * Comma-separated list of artifacts to be excluded in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "excludes", defaultValue = "")
  private List<String> excludes;

  /**
   * The path to the generated dot file.
   *
   * @since 1.0.0
   */
  @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/" + OUTPUT_DOT_FILE_NAME)
  private File outputFile;

  /**
   * If set to true and Graphviz is installed on the system where this plugin is executed, the dot file will be
   * converted to a graph image using Graphviz' dot executable.
   *
   * @see #imageFormat
   * @since 1.0.0
   */
  @Parameter(property = "createImage", defaultValue = "false")
  private boolean createImage;

  /**
   * The format for the graph image when {@link #createImage} is set to {@code true}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "imageFormat", defaultValue = "png")
  private String imageFormat;


  /**
   * Local maven repository required by the {@link DependencyTreeBuilder}.
   */
  @Parameter(defaultValue = "${localRepository}", readonly = true)
  ArtifactRepository localRepository;

  @Component
  private MavenProject project;

  @Component(hint = "default")
  DependencyGraphBuilder dependencyGraphBuilder;

  @Component
  DependencyTreeBuilder dependencyTreeBuilder;

  @Override
  public void execute() throws MojoExecutionException {
    ArtifactFilter filter = createArtifactFilter();

    try {
      GraphFactory graphFactory = createGraphFactory(filter);

      writeDotFile(graphFactory.createGraph(this.project));

      if (this.createImage) {
        createGraphImage();
      }

    } catch (DependencyGraphException e) {
      throw new MojoExecutionException("Unable to create dependency graph.", e.getCause());
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to write graph file.", e);
    }
  }

  protected abstract GraphFactory createGraphFactory(ArtifactFilter artifactFilter);

  private ArtifactFilter createArtifactFilter() {
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

  private void writeDotFile(String dotGraph) throws IOException {
    Files.createParentDirs(this.outputFile);

    try (Writer writer = Files.newWriter(this.outputFile, Charsets.UTF_8)) {
      writer.write(dotGraph);
    }
  }

  private void createGraphImage() throws IOException {
    String graphFileName = createGraphFileName();

    Path graphFile = this.outputFile.toPath().getParent().resolve(graphFileName);
    List<String> commandLine = Arrays.asList(
        "dot",
        "-T", this.imageFormat,
        "-o", graphFile.toAbsolutePath().toString(),
        this.outputFile.getAbsolutePath());

    getLog().info("Running Graphviz: " + Joiner.on(" ").join(commandLine));

    Process process = new ProcessBuilder(commandLine)
      .redirectErrorStream(true)
      .start();

    List<String> output = CharStreams.readLines(new InputStreamReader(process.getInputStream()));

    try {
      int exitCode = process.waitFor();

      for (String line : output) {
        getLog().info("  dot> " + line);
      }

      if (exitCode != 0) {
        throw new IOException("Graphviz terminated abnormally. Exit code: " + exitCode);
      }

      getLog().info("Graph image created on " + graphFile.toAbsolutePath());

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      process.destroy();

      throw new IOException("Graph image creation interrupted", e);
    }

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
