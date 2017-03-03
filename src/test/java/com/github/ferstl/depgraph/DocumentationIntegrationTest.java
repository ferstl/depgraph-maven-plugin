package com.github.ferstl.depgraph;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Locale;

import static io.takari.maven.testing.TestResources.assertFilesPresent;
import static org.junit.Assume.assumeTrue;

/**
 * Integration test that creates the images for this plugin's README.MD documentation.
 */
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions("3.3.9")
public class DocumentationIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public DocumentationIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Test
  public void documentationSimpleGraph() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithVersions() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .withCliOption("-DshowVersions=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithGroupIds() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .withCliOption("-DshowGroupIds=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithDuplicatesAndConflicts() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .withCliOption("-DshowVersions=true")
        .withCliOption("-DshowDuplicates=true")
        .withCliOption("-DshowConflictes=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationAggregated() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DincludeParentProjects=true")
        .withCliOption("-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3")
        .withCliOptions("-DcreateImage=true")
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.png");
  }

  @Test
  public void documentationAggregatedByGroupId() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .execute("clean", "package", "depgraph:aggregate-by-groupid");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.png");
  }

  @Test
  public void documentationCustomStyle() throws Exception {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    String styleConfiguration = basedir.toPath().resolve("custom-style.json").toAbsolutePath().toString();
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOptions("-DcreateImage=true")
        .withCliOption("-DshowGroupIds=true")
        .withCliOption("-DincludeParentProjects=true")
        .withCliOption("-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3")
        .withCliOption("-DcustomStyleConfiguration=" + styleConfiguration)
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.png");
  }

  private static boolean isGraphvizInstalled() {
    return getDotExecutable() != null;
  }

  private static String getDotExecutable() {
    Commandline cmd = new Commandline();
    String finderExecutable = isWindows() ? "where.exe" : "which";

    cmd.setExecutable(finderExecutable);
    cmd.addArguments(new String[]{"dot"});

    CommandLineUtils.StringStreamConsumer systemOut = new CommandLineUtils.StringStreamConsumer();
    CommandLineUtils.StringStreamConsumer systemErr = new CommandLineUtils.StringStreamConsumer();

    try {
      int exitCode = CommandLineUtils.executeCommandLine(cmd, systemOut, systemErr);
      if (exitCode != 0) {
        return null;
      }
    } catch (CommandLineException e) {
      return null;
    }

    return systemOut.getOutput();
  }

  private static boolean isWindows() {
    String osName = System.getProperty("os.name", "n/a");
    return osName.toLowerCase(Locale.US).contains("windows");
  }
}
