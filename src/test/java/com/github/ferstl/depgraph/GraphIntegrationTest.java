package com.github.ferstl.depgraph;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Locale;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

import static io.takari.maven.testing.TestResources.assertFileContents;
import static io.takari.maven.testing.TestResources.assertFilesPresent;
import static org.junit.Assume.assumeTrue;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions("3.3.9")
public class GraphIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public GraphIntegrationTest(MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void graphInDot() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "sub-parent/module-3/target/dependency-graph.dot",
        // not wanted in the future
        "target/dependency-graph.dot",
        "sub-parent/target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_module-1.dot", "module-1/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-2.dot", "module-2/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-3.dot", "sub-parent/module-3/target/dependency-graph.dot");
  }

  @Test
  public void graphWithImageFile() throws Exception {
    // Skip if graviz is not installed
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
  public void graphInGml() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .withCliOption("-DshowGroupIds=true")
        .withCliOption("-DshowVersions=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.gml",
        "module-2/target/dependency-graph.gml",
        "sub-parent/module-3/target/dependency-graph.gml",
        // not wanted in the future
        "target/dependency-graph.gml",
        "sub-parent/target/dependency-graph.gml");

    assertFileContents(basedir, "expectations/graph_module-1.gml", "module-1/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-2.gml", "module-2/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-3.gml", "sub-parent/module-3/target/dependency-graph.gml");
  }

  private boolean isGraphvizInstalled() {
    return getDotExecutable() != null;
  }

  private String getDotExecutable() {
    Commandline cmd = new Commandline();
    String finderExecutable = isWindows() ? "where.exe" : "which";

    cmd.setExecutable(finderExecutable);
    cmd.addArguments(new String[]{"dot"});

    StringStreamConsumer systemOut = new StringStreamConsumer();
    StringStreamConsumer systemErr = new StringStreamConsumer();

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

  private boolean isWindows() {
    String osName = System.getProperty("os.name", "n/a");
    return osName.toLowerCase(Locale.US).contains("windows");
  }
}
