/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectDependenciesResolver;
import org.apache.maven.shared.artifact.filter.ScopeArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternExcludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;
import com.github.ferstl.depgraph.dependency.DependencyGraphException;
import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.dot.DotGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.dot.style.StyleConfiguration;
import com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource;
import com.github.ferstl.depgraph.dependency.dot.style.resource.ClasspathStyleResource;
import com.github.ferstl.depgraph.dependency.dot.style.resource.FileSystemStyleResource;
import com.github.ferstl.depgraph.dependency.dot.style.resource.StyleResource;
import com.github.ferstl.depgraph.dependency.gml.GmlGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.json.JsonGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.puml.PumlGraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.text.TextGraphStyleConfigurer;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import static com.github.ferstl.depgraph.GraphFormat.JSON;

/**
 * Abstract mojo to create all possible kinds of graphs. Graphs are created with instances of the
 * {@link GraphFactory} interface. This class defines an abstract method to create such factories. In case Graphviz is
 * installed on the system where this plugin is executed, it is also possible to run the dot program and create images
 * out of the generated dot files. Besides that, this class allows the configuration of several basic mojo parameters,
 * such as includes, excludes, etc.
 */
abstract class AbstractGraphMojo extends AbstractMojo {

  private static final Pattern LINE_SEPARATOR_PATTERN = Pattern.compile("\r?\n");
  private static final String OUTPUT_FILE_NAME = "dependency-graph";

  /**
   * The scope of the artifacts that should be included in the graph. An empty string indicates all scopes (default).
   * The scopes being interpreted are the scopes as Maven sees them, not as specified in the pom. In summary:
   * <ul>
   * <li>{@code compile}: Shows compile, provided and system dependencies</li>
   * <li>{@code provided}: Shows provided dependencies</li>
   * <li>{@code runtime}: Shows compile and runtime dependencies</li>
   * <li>{@code system}: Shows system dependencies</li>
   * <li>{@code test} (default): Shows all dependencies</li>
   * </ul>
   *
   * @since 1.0.0
   */
  @Parameter(property = "scope")
  private String scope;

  /**
   * List of artifacts to be included in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "includes")
  private List<String> includes;

  /**
   * List of artifacts to be excluded in the form of {@code groupId:artifactId:type:classifier}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "excludes")
  private List<String> excludes;

  /**
   * List of artifacts in the form of {@code groupId:artifactId:type:classifier} to be included if they are
   * <strong>transitive</strong>.
   *
   * @since 3.0.0
   */
  @Parameter(property = "transitiveIncludes")
  private List<String> transitiveIncludes;

  /**
   * List of artifacts in the form of {@code groupId:artifactId:type:classifier} to be excluded if they are
   * <strong>transitive</strong>.
   *
   * @since 3.0.0
   */
  @Parameter(property = "transitiveExcludes")
  private List<String> transitiveExcludes;

  /**
   * List of artifacts, in the form of {@code groupId:artifactId:type:classifier}, to restrict the dependency graph
   * only to artifacts that depend on them.
   *
   * @since 1.0.4
   */
  @Parameter(property = "targetIncludes")
  private List<String> targetIncludes;

  /**
   * Indicates whether optional dependencies should be excluded from the graph.
   *
   * @since 3.2.0
   */
  @Parameter(property = "excludeOptionalDependencies", defaultValue = "false")
  private boolean excludeOptionalDependencies;

  /**
   * Format of the graph, either &quot;dot&quot; (default), &quot;gml&quot;, &quot;puml&quot;, &quot;json&quot; or &quot;text&quot;.
   *
   * @since 2.1.0
   */
  @Parameter(property = "graphFormat", defaultValue = "dot")
  private String graphFormat;

  /**
   * If set to {@code true} (which is the default) <strong>and</strong> the graph format is 'json', the graph will show
   * any information that is possible.
   * The idea behind this option is, that the consumer of the JSON data, for example a Javascript library, will do its
   * own filtering of the data.
   *
   * @since 3.0.0
   */
  @Parameter(property = "showAllAttributesForJson", defaultValue = "true")
  private boolean showAllAttributesForJson;

  /**
   * Output directory to write the dependency graph to.
   *
   * @since 2.2.0
   */
  @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}")
  private File outputDirectory;

  /**
   * The name of the dependency graph file. A file extension matching the configured {@code graphFormat} will be
   * added if not specified.
   *
   * @since 2.2.0
   */
  @Parameter(property = "outputFileName", defaultValue = OUTPUT_FILE_NAME)
  private String outputFileName;

  /**
   * Indicates whether the project's artifact ID should be used as file name for the generated graph files.
   * <ul>
   * <li>This flag does not have an effect when the (deprecated) {@code outputFile} parameter is used.</li>
   * <li>When set to {@code true}, the content of the {@code outputFileName} parameter is ignored.</li>
   * </ul>
   *
   * @since 2.2.0
   */
  @Parameter(property = "useArtifactIdInFileName", defaultValue = "false")
  private boolean useArtifactIdInFileName;

  /**
   * Only relevant when {@code graphFormat=dot}: If set to {@code true} and Graphviz is installed on the system where
   * this plugin is executed, the dot file will be converted to a graph image using Graphviz' dot executable.
   *
   * @see #imageFormat
   * @see #dotExecutable
   * @since 1.0.0
   */
  @Parameter(property = "createImage", defaultValue = "false")
  private boolean createImage;

  /**
   * Only relevant when {@code graphFormat=dot}: The format for the graph image when {@link #createImage} is set to
   * {@code true}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "imageFormat", defaultValue = "png")
  private String imageFormat;

  /**
   * Only relevant when {@code graphFormat=dot}: Path to the dot executable. Use this option in case
   * {@link #createImage} is set to {@code true} and the dot executable is not on the system {@code PATH}.
   *
   * @since 1.0.0
   */
  @Parameter(property = "dotExecutable")
  private File dotExecutable;

  /**
   * Only relevant when {@code graphFormat=dot}: Path to a custom style configuration in JSON format.
   *
   * @since 2.0.0
   */
  @Parameter(property = "customStyleConfiguration")
  private String customStyleConfiguration;

  /**
   * Only relevant when {@code graphFormat=dot}: If set to {@code true} the effective style configuration used to
   * create this graph will be printed on the console.
   *
   * @since 2.0.0
   */
  @Parameter(property = "printStyleConfiguration", defaultValue = "false")
  private boolean printStyleConfiguration;

  /**
   * Skip execution when set to {@true}.
   *
   * @since 3.3.0
   */
  @Parameter(property = "depgraph.skip", defaultValue = "false")
  private boolean skip;


  /**
   * The project's artifact ID.
   */
  @Parameter(defaultValue = "${project.artifactId}", readonly = true)
  private String artifactId;

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  @Component
  ProjectDependenciesResolver dependenciesResolver;

  @Override
  public final void execute() throws MojoExecutionException, MojoFailureException {
    if (this.skip) {
      getLog().info("Skipping execution");
      return;
    }

    GraphFormat graphFormat = GraphFormat.forName(this.graphFormat);
    ArtifactFilter globalFilter = createGlobalArtifactFilter();
    ArtifactFilter transitiveIncludeExcludeFilter = createTransitiveIncludeExcludeFilter();
    ArtifactFilter targetFilter = createTargetArtifactFilter();
    GraphStyleConfigurer graphStyleConfigurer = createGraphStyleConfigurer(graphFormat);
    Path graphFilePath = createGraphFilePath(graphFormat);

    try {
      GraphFactory graphFactory = createGraphFactory(globalFilter, transitiveIncludeExcludeFilter, targetFilter, graphStyleConfigurer);
      String dependencyGraph = graphFactory.createGraph(getProject());
      writeGraphFile(dependencyGraph, graphFilePath);

      if (graphFormat == GraphFormat.DOT && this.createImage) {
        createDotGraphImage(graphFilePath);
      } else if (graphFormat == GraphFormat.TEXT) {
        getLog().info("Dependency graph:\n" + dependencyGraph);
      }

    } catch (DependencyGraphException e) {
      throw new MojoExecutionException("Unable to create dependency graph.", e.getCause());
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to write graph file.", e);
    }
  }

  protected abstract GraphFactory createGraphFactory(ArtifactFilter globalFilter, ArtifactFilter transitiveIncludeExcludeFilter, ArtifactFilter targetFilter, GraphStyleConfigurer graphStyleConfigurer);

  /**
   * Override this method to configure additional style resources. It is recommendet to call
   * {@code super.getAdditionalStyleResources()} and add them to the set.
   *
   * @return A set of additional built-in style resources to use.
   */
  protected Set<BuiltInStyleResource> getAdditionalStyleResources() {
    // We need to preserve the order of style configurations
    return new LinkedHashSet<>();
  }

  /**
   * Indicates to subclasses that everything possible should be shown in the graph, no matter what was configured
   * for the specific mojo.
   *
   * @return {@code true} if the full graph should be shown, {@code false} else.
   */
  protected boolean showFullGraph() {
    return GraphFormat.forName(this.graphFormat) == JSON && this.showAllAttributesForJson;
  }

  protected MavenProject getProject() {
    return this.project;
  }

  private ArtifactFilter createGlobalArtifactFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (this.scope != null) {
      filter.add(new ScopeArtifactFilter(this.scope));
    }

    if (!this.includes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.includes));
    }

    if (!this.excludes.isEmpty()) {
      filter.add(new StrictPatternExcludesArtifactFilter(this.excludes));
    }

    if (this.excludeOptionalDependencies) {
      filter.add(new OptionalArtifactFilter());
    }

    return filter;
  }

  private ArtifactFilter createTransitiveIncludeExcludeFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (!this.transitiveIncludes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.transitiveIncludes));
    }

    if (!this.transitiveExcludes.isEmpty()) {
      filter.add(new StrictPatternExcludesArtifactFilter(this.transitiveExcludes));
    }

    return filter;
  }

  private ArtifactFilter createTargetArtifactFilter() {
    AndArtifactFilter filter = new AndArtifactFilter();

    if (!this.targetIncludes.isEmpty()) {
      filter.add(new StrictPatternIncludesArtifactFilter(this.targetIncludes));
    }

    return filter;
  }

  private GraphStyleConfigurer createGraphStyleConfigurer(GraphFormat graphFormat) throws MojoFailureException {
    switch (graphFormat) {
      case DOT:
        StyleConfiguration styleConfiguration = loadStyleConfiguration();
        return new DotGraphStyleConfigurer(styleConfiguration);
      case GML:
        return new GmlGraphStyleConfigurer();
      case PUML:
        return new PumlGraphStyleConfigurer();
      case JSON:
        return new JsonGraphStyleConfigurer();
      case TEXT:
        return new TextGraphStyleConfigurer();
      default:
        throw new IllegalArgumentException("Unsupported output format: " + graphFormat);
    }
  }

  private StyleConfiguration loadStyleConfiguration() throws MojoFailureException {
    // default style resources
    ClasspathStyleResource defaultStyleResource = BuiltInStyleResource.DEFAULT_STYLE.createStyleResource(getClass().getClassLoader());

    // additional style resources from the mojo
    Set<StyleResource> styleResources = new LinkedHashSet<>();
    for (BuiltInStyleResource additionalResource : getAdditionalStyleResources()) {
      styleResources.add(additionalResource.createStyleResource(getClass().getClassLoader()));
    }

    // custom style resource
    if (StringUtils.isNotBlank(this.customStyleConfiguration)) {
      StyleResource customStyleResource = getCustomStyleResource();
      getLog().info("Using custom style configuration " + customStyleResource);
      styleResources.add(customStyleResource);
    }

    // load and print
    StyleConfiguration styleConfiguration = StyleConfiguration.load(defaultStyleResource, styleResources.toArray(new StyleResource[0]));
    if (this.printStyleConfiguration) {
      getLog().info("Using effective style configuration:\n" + styleConfiguration.toJson());
    }

    return styleConfiguration;
  }

  private StyleResource getCustomStyleResource() throws MojoFailureException {
    StyleResource customStyleResource;
    if (StringUtils.startsWith(this.customStyleConfiguration, "classpath:")) {
      String resourceName = StringUtils.substring(this.customStyleConfiguration, 10, this.customStyleConfiguration.length());
      customStyleResource = new ClasspathStyleResource(resourceName, getClass().getClassLoader());
    } else {
      customStyleResource = new FileSystemStyleResource(Paths.get(this.customStyleConfiguration));
    }

    if (!customStyleResource.exists()) {
      throw new MojoFailureException("Custom configuration '" + this.customStyleConfiguration + "' does not exist.");
    }

    return customStyleResource;
  }

  private Path createGraphFilePath(GraphFormat graphFormat) {
    String fileName = this.useArtifactIdInFileName ? this.artifactId : this.outputFileName;
    fileName = addFileExtensionIfNeeded(graphFormat, fileName);

    // ${project.build.directory} is not resolved when run without a POM file (e.g. for the for-artifact goal)
    if (isOutputDirectoryResolved()) {
      return this.outputDirectory.toPath().resolve(fileName);
    }

    return Paths.get(System.getProperty("user.dir"), fileName);
  }

  private String addFileExtensionIfNeeded(GraphFormat graphFormat, String fileName) {
    String fileExtension = graphFormat.getFileExtension();

    if (!fileName.endsWith(fileExtension)) {
      fileName += fileExtension;
    }
    return fileName;
  }

  private boolean isOutputDirectoryResolved() {
    return !this.outputDirectory.toString().contains("${project.basedir}");
  }

  private void writeGraphFile(String graph, Path graphFilePath) throws IOException {
    Path parent = graphFilePath.getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    try (Writer writer = Files.newBufferedWriter(graphFilePath, StandardCharsets.UTF_8)) {
      writer.write(graph);
    }
  }

  private void createDotGraphImage(Path graphFilePath) throws IOException {
    String graphFileName = createDotImageFileName(graphFilePath);
    Path graphFile = graphFilePath.resolveSibling(graphFileName);

    String dotExecutable = determineDotExecutable();
    String[] arguments = new String[]{
        "-T", this.imageFormat,
        "-o", graphFile.toAbsolutePath().toString(),
        graphFilePath.toAbsolutePath().toString()};

    Commandline cmd = new Commandline();
    cmd.setExecutable(dotExecutable);
    cmd.addArguments(arguments);

    getLog().info("Running Graphviz: " + dotExecutable + " " + Joiner.on(" ").join(arguments));

    StringStreamConsumer systemOut = new StringStreamConsumer();
    StringStreamConsumer systemErr = new StringStreamConsumer();
    int exitCode;

    try {
      exitCode = CommandLineUtils.executeCommandLine(cmd, systemOut, systemErr);
    } catch (CommandLineException e) {
      throw new IOException("Unable to execute Graphviz", e);
    }

    Splitter lineSplitter = Splitter.on(LINE_SEPARATOR_PATTERN).omitEmptyStrings().trimResults();
    Iterable<String> output = Iterables.concat(
        lineSplitter.split(systemOut.getOutput()),
        lineSplitter.split(systemErr.getOutput()));

    for (String line : output) {
      getLog().info("  dot> " + line);
    }

    if (exitCode != 0) {
      throw new IOException("Graphviz terminated abnormally. Exit code: " + exitCode);
    }

    getLog().info("Graph image created on " + graphFile.toAbsolutePath());
  }

  private String createDotImageFileName(Path graphFilePath) {
    String graphFileName = graphFilePath.getFileName().toString();

    if (graphFileName.endsWith(GraphFormat.DOT.getFileExtension())) {
      graphFileName = graphFileName.substring(0, graphFileName.lastIndexOf(".")) + "." + this.imageFormat;
    } else {
      graphFileName = graphFileName + this.imageFormat;
    }

    return graphFileName;
  }

  private String determineDotExecutable() throws IOException {
    if (this.dotExecutable == null) {
      return "dot";
    }

    Path dotExecutablePath = this.dotExecutable.toPath();
    if (!Files.exists(dotExecutablePath)) {
      throw new NoSuchFileException("The dot executable '" + this.dotExecutable + "' does not exist.");
    } else if (Files.isDirectory(dotExecutablePath) || !Files.isExecutable(dotExecutablePath)) {
      throw new IOException("The dot executable '" + this.dotExecutable + "' is not a file or cannot be executed.");
    }

    return dotExecutablePath.toAbsolutePath().toString();
  }

  private static class OptionalArtifactFilter implements ArtifactFilter {

    @Override
    public boolean include(Artifact artifact) {
      return !artifact.isOptional();
    }
  }
}
