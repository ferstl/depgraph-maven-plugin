/*
 * Copyright (c) 2014 - 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import static com.github.ferstl.depgraph.MavenVersion.MAX_VERSION;
import static com.github.ferstl.depgraph.MavenVersion.MIN_VERSION;
import static io.takari.maven.testing.TestResources.assertFileContents;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({MAX_VERSION, MIN_VERSION})
public class ForArtifactIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;
  private final TestProperties testProperties;

  public ForArtifactIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.testProperties = new TestProperties();
    this.mavenRuntime = builder
        .withCliOptions("-B")
        .build();
  }

  @Test
  public void runWithoutProject() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
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
        .withCliOption("-DgroupId=com.google.guava")
        .withCliOption("-DartifactId=guava")
        .withCliOption("-Dversion=27.0-jre")
        .withCliOption("-DshowGroupIds")
        .withCliOption("-DshowVersions")
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DclasspathScope=runtime")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertErrorFreeLog();
    assertFileContents(basedir, "expectations/guava.txt", "target/dependency-graph.txt");
  }

  @Test
  public void runWithInexistentArtifact() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgroupId=com.google.guava")
        .withCliOption("-DartifactId=guava")
        .withCliOption("-Dversion=000000abxdce")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertLogText("[ERROR] Failed to execute goal com.github.ferstl:depgraph-maven-plugin");
    result.assertLogText("com.google.guava:guava:jar:000000abxdce:compile");
  }

  @Test
  public void runWithArtifactParameter() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-Dartifact=org.springframework:spring-jdbc:5.1.3.RELEASE")
        .withCliOption("-DshowVersions")
        .withCliOption("-DgraphFormat=text")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertErrorFreeLog();
    assertFileContents(basedir, "expectations/spring-jdbc.txt", "dependency-graph.txt");
  }

  @Test
  public void runWithArtifactAndGavParameters() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-Dartifact=org.springframework:spring-jdbc:5.1.3.RELEASE")
        .withCliOption("-DgroupId=org.springframework")
        .withCliOption("-DartifactId=spring-jdbc")
        .withCliOption("-Dversion=5.1.3.RELEASE")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertLogText("[ERROR] Failed to execute goal com.github.ferstl:depgraph-maven-plugin");
    result.assertLogText("Artifact can be defined with either 'artifact' or 'groupId'/'artifactId'/'version' but not both");
  }

  @Test
  public void runWithInvalidArtifactParameter() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-Dartifact=org.springframework:spring-jdbc")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertLogText("[ERROR] Failed to execute goal com.github.ferstl:depgraph-maven-plugin");
    result.assertLogText("Invalid artifact definition: org.springframework:spring-jdbc");
  }

  @Test
  public void runWithIncompleteGavParameters() throws Exception {
    // arrange
    File basedir = this.resources.getBasedir("no-project");

    // act
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgroupId=com.google.guava")
        .withCliOption("-Dversion=27.0-jre")
        .execute(createFullyQualifiedGoal());

    // assert
    result.assertLogText("[ERROR] Failed to execute goal com.github.ferstl:depgraph-maven-plugin");
    result.assertLogText("'groupId', 'artifactId' and 'version' parameters have to be defined");
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
