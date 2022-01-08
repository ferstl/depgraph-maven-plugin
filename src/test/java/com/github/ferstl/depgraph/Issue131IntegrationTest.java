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

/**
 * Integration test for Issue #131 where a conflicting dependency introduced a circle and lead to
 * a {@code StackOverflowError}.
 */
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.5.2", "3.3.9"})
public class Issue131IntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public Issue131IntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void circularDependency() throws Exception {
    File basedir = this.resources.getBasedir("issue-131");
    //mvn -N com.github.ferstl:depgraph-maven-plugin:graph -DgraphFormat=text -DshowDuplicates -DshowConflicts -DshowVersions -DshowGroupIds
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-N")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DshowDuplicates")
        .withCliOption("-DshowConflicts")
        .withCliOption("-DshowVersions")
        .withCliOption("-DshowGroupIds")
        .execute("clean", "package", "depgraph:graph");

    result.assertErrorFreeLog();
  }

}
