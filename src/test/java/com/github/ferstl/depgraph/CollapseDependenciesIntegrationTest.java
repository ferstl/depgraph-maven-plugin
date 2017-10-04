package com.github.ferstl.depgraph;

import java.io.File;
import java.nio.file.FileSystems;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

@Ignore
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions("3.5.0")
public class CollapseDependenciesIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public CollapseDependenciesIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void collapse() throws Exception {
    File basedir = this.resources.getBasedir("collapse-dependencies-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DcreateImage")
        .execute("clean", "package", "depgraph:aggregate");

    result.assertErrorFreeLog();
  }

  @Test
  public void graph() throws Exception {
    File basedir = this.resources.getBasedir("collapse-dependencies-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DcreateImage")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
  }

}
