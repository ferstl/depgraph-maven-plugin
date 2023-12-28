/*
 * Copyright (c) 2014 - 2024 the original author or authors.
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
public class ReducedEdgesIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public ReducedEdgesIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
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
  public void aggregateWithReducedEdges() throws Exception {
    File basedir = this.resources.getBasedir("reduced-edges-test");
    // Reduced edges are the default
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregated-reduced-edges.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateWithAllEdges() throws Exception {
    File basedir = this.resources.getBasedir("reduced-edges-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DreduceEdges=false")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregated-all-edges.dot", "target/dependency-graph.dot");
  }
}
