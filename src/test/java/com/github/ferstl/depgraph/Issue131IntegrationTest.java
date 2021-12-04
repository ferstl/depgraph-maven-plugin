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

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({MAX_VERSION, MIN_VERSION})
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
  public void test() throws Exception {
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
