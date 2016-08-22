package com.github.ferstl.depgraph;

import java.io.File;
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

  @Test
  public void graph() throws Exception {
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
}
