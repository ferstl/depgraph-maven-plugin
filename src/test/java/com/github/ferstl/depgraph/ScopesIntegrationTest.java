/*
 * Copyright (c) 2014 - 2022 the original author or authors.
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
    this.mavenRuntime = builder
        .withCliOptions("-B")
        .build();
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
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "depgraph:graph");

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
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=provided")
        .execute("clean", "depgraph:graph");

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
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DclasspathScope=test")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    result.assertLogText("The 'classpathScope' parameter will be ignored");
    assertFileContents(basedir, "expectations/module-d-compile-scope.txt", "module-d/target/dependency-graph.txt");
  }

  @Test
  public void aggregatedCompile() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=compile")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();

    assertFilesPresent(basedir, "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/aggregated-compile.txt", "target/dependency-graph.txt");
  }

  @Test
  public void aggregatedProvidedAndTest() throws Exception {
    File basedir = this.resources.getBasedir("scopes-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-Dscopes=provided,test")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();

    assertFilesPresent(basedir, "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/aggregated-provided-and-test.txt", "target/dependency-graph.txt");
  }
}
