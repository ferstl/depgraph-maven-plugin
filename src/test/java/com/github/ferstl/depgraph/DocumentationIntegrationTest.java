package com.github.ferstl.depgraph;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecution;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
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
  private File basedir;

  public DocumentationIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() throws IOException {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());
    this.basedir = getBaseDir();
  }

  @Test
  public void documentationSimpleGraph() throws Exception {
    runTest("graph", "-DcreateImage=true");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithVersions() throws Exception {
    runTest("graph", "-DcreateImage=true", "-DshowVersions=true");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithGroupIds() throws Exception {
    runTest("graph", "-DcreateImage=true", "-DshowGroupIds=true");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationWithDuplicatesAndConflicts() throws Exception {
    runTest("graph",
        "-DcreateImage=true",
        "-DshowVersions=true",
        "-DshowDuplicates=true",
        "-DshowConflictes=true");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        // not wanted in the future
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");
  }

  @Test
  public void documentationAggregated() throws Exception {
    runTest("aggregate",
        "-DcreateImage=true",
        "-DincludeParentProjects=true",
        "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3");

    assertFilesPresent(this.basedir, "target/dependency-graph.png");
  }

  @Test
  public void documentationAggregatedByGroupId() throws Exception {
    runTest("aggregate-by-groupid", "-DcreateImage=true");

    assertFilesPresent(this.basedir, "target/dependency-graph.png");
  }

  @Test
  public void documentationCustomStyle() throws Exception {
    String styleConfiguration = this.basedir.toPath().resolve("custom-style.json").toAbsolutePath().toString();
    runTest(
        "aggregate",
        "-DcreateImage=true",
        "-DshowGroupIds=true",
        "-DincludeParentProjects=true",
        "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3",
        "-DcustomStyleConfiguration=" + styleConfiguration);

    assertFilesPresent(this.basedir, "target/dependency-graph.png");
  }

  private void runTest(String goal, String... cliOptions) throws Exception {
    File basedir = getBaseDir();
    MavenExecution execution = this.mavenRuntime.forProject(basedir);
    for (String cliOption : cliOptions) {
      execution.withCliOption(cliOption);
    }

    MavenExecutionResult result = execution.execute("clean", "package", "depgraph:" + goal);
    result.assertErrorFreeLog();
  }

  private File getBaseDir() throws IOException {
    return this.resources.getBasedir("depgraph-maven-plugin-test");
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
