package com.github.ferstl.depgraph;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.takari.maven.testing.TestProperties;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import static io.takari.maven.testing.TestResources.assertFileContents;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.6.0"})
public class ForArtifactIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;
  private final TestProperties testProperties;

  public ForArtifactIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.testProperties = new TestProperties();
    this.mavenRuntime = builder.build();
  }

  @Test
  public void runWithoutProject() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgroupId=org.springframework")
        .withCliOption("-DartifactId=spring-jdbc")
        .withCliOption("-Dversion=5.1.3.RELEASE")
        .withCliOption("-DshowVersions")
        .withCliOption("-DgraphFormat=text")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertErrorFreeLog();
    assertFileContents(basedir, "expectations/spring-jdbc.txt", "dependency-graph.txt");
  }

  @Test
  public void runWithProject() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("single-dependency");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgroupId=com.google.guava")
        .withCliOption("-DartifactId=guava")
        .withCliOption("-Dversion=27.0-jre")
        .withCliOption("-DshowGroupIds")
        .withCliOption("-DshowVersions")
        .withCliOption("-DgraphFormat=text")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertErrorFreeLog();
    assertFileContents(basedir, "expectations/guava.txt", "target/dependency-graph.txt");
  }

  /**
   * Helper to create a fully qualified Maven goal with the curren plugin version. This is needed for tests
   * without POM files where {@code it-plugin.version} can not be injected.
   *
   * @return The fully qualified goal {@code <groupId>:<artifactId>:<version>:for-artifact}
   */
  private String createFullyQualifiedGoal() {
    return this.testProperties.get("project.groupId") + ":"
        + this.testProperties.get("project.artifactId") + ":"
        + this.testProperties.get("project.version") + ":"
        + "for-artifact";
  }
}
