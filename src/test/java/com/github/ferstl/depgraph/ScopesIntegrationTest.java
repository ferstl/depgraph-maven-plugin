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
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import static com.github.ferstl.depgraph.MavenVersion.MAX_VERSION;
import static com.github.ferstl.depgraph.MavenVersion.MIN_VERSION;
import static io.takari.maven.testing.TestResources.assertFileContents;
import static io.takari.maven.testing.TestResources.assertFilesPresent;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({MAX_VERSION, MIN_VERSION})
public class ScopesIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public ScopesIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void compileOnly() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();

    assertFilesPresent(
        basedir,
        "module-a/target/dependency-graph.txt",
        "module-b/target/dependency-graph.txt",
        "module-c/target/dependency-graph.txt",
        "module-d/target/dependency-graph.txt"
    );

    assertFileContents(basedir, "expectations/module-a-compile-scope.txt", "module-a/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-b-compile-scope.txt", "module-b/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-c-compile-scope.txt", "module-c/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-d-compile-scope.txt", "module-d/target/dependency-graph.txt");
  }

  @Test
  public void providedOnly() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=provided")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();

    assertFilesPresent(
        basedir,
        "module-a/target/dependency-graph.txt",
        "module-b/target/dependency-graph.txt",
        "module-c/target/dependency-graph.txt",
        "module-d/target/dependency-graph.txt"
    );

    assertFileContents(basedir, "expectations/module-a-provided-scope.txt", "module-a/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-b-provided-scope.txt", "module-b/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-c-provided-scope.txt", "module-c/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/module-d-provided-scope.txt", "module-d/target/dependency-graph.txt");
  }

  @Test
  public void classpathScopeIsIgnored() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DclasspathScope=test")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
    result.assertLogText("The 'classpathScope' parameter will be ignored");
    assertFileContents(basedir, "expectations/module-d-compile-scope.txt", "module-d/target/dependency-graph.txt");
  }

  @Test
  public void aggregatedCompile() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();

    assertFilesPresent(basedir, "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/aggregated-compile.txt", "target/dependency-graph.txt");
  }

  @Test
  public void aggregatedProvidedAndTest() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=provided,test")
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();

    assertFilesPresent(basedir, "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/aggregated-provided-and-test.txt", "target/dependency-graph.txt");
  }
}
