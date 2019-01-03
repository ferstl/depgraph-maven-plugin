package com.github.ferstl.depgraph;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.6.0"})
public class FromArtifactMojoIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public FromArtifactMojoIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Test
  public void withoutProject() throws Exception {
    File basedir = this.resources.getBasedir("no-project");

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-B")
        .withCliOption("-DgroupId=com.google.guava")
        .withCliOption("-DartifactId=guava")
        .withCliOption("-Dversion=27.0.1-jre")
        .execute("com.github.ferstl:depgraph-maven-plugin:3.3.0-SNAPSHOT:from-artifact");

    result.assertErrorFreeLog();
  }
}
