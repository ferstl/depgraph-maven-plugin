package com.github.ferstl.depgraph;

import java.io.File;
import java.nio.file.FileSystems;
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
        "target/dependency-graph.dot",
        "sub-parent/target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_parent.dot", "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-1.dot", "module-1/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-2.dot", "module-2/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_sub-parent.dot", "sub-parent/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-3.dot", "sub-parent/module-3/target/dependency-graph.dot");
  }

  @Test
  public void byGroupIdInDot() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "package", "depgraph:by-groupid");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "sub-parent/module-3/target/dependency-graph.dot",
        "target/dependency-graph.dot",
        "sub-parent/target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/by-groupid_parent.dot", "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-1.dot", "module-1/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-2.dot", "module-2/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_sub-parent.dot", "sub-parent/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-3.dot", "sub-parent/module-3/target/dependency-graph.dot");
  }

  @Test
  public void exampleInDot() throws Exception {
    File basedir = this.resources.getBasedir("no-dependencies");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "package", "depgraph:example");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/example.dot", "target/dependency-graph.dot");
  }

  @Test
  public void customGraphStyle() throws Exception {
    File basedir = this.resources.getBasedir("single-dependency");
    String styleConfiguration = basedir.toPath().resolve("graph-style.json").toAbsolutePath().toString();

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DcustomStyleConfiguration=" + styleConfiguration)
        .withCliOption("-DcreateImage=true")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_custom-graph-style.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateWithoutDependencies() throws Exception {
    File basedir = this.resources.getBasedir("no-dependencies");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/aggregate-without-dependencies.gml", "target/dependency-graph.gml");
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
        "target/dependency-graph.gml",
        "sub-parent/target/dependency-graph.gml");

    assertFileContents(basedir, "expectations/graph_parent.gml", "target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-1.gml", "module-1/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-2.gml", "module-2/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_sub-parent.gml", "sub-parent/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-3.gml", "sub-parent/module-3/target/dependency-graph.gml");
  }

}
