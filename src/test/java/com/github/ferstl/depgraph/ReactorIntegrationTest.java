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
import io.takari.maven.testing.executor.MavenRuntime.MavenRuntimeBuilder;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import static com.github.ferstl.depgraph.MavenVersion.MAX_VERSION;
import static com.github.ferstl.depgraph.MavenVersion.MIN_VERSION;
import static io.takari.maven.testing.TestResources.assertFileContents;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({MIN_VERSION, MAX_VERSION})
public class ReactorIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public ReactorIntegrationTest(MavenRuntimeBuilder builder) throws Exception {
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
  public void reactorText() throws Exception {
    File basedir = this.resources.getBasedir("reactor-graph");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .execute("clean", "depgraph:reactor");

    result.assertErrorFreeLog();

    assertFileContents(basedir, "expectations/reactor.txt", "target/dependency-graph.txt");
  }

  @Test
  public void reactorDot() throws Exception {
    File basedir = this.resources.getBasedir("reactor-graph");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=dot")
        .execute("clean", "depgraph:reactor");

    result.assertErrorFreeLog();

    assertFileContents(basedir, "expectations/reactor.dot", "target/dependency-graph.dot");
  }

  // Issue #144
  @Test
  public void reactorPuml() throws Exception {
    File basedir = this.resources.getBasedir("reactor-graph");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=puml")
        .execute("clean", "depgraph:reactor");

    result.assertErrorFreeLog();

    assertFileContents(basedir, "expectations/reactor.puml", "target/dependency-graph.puml");
  }

  @Test
  public void reactorGml() throws Exception {
    File basedir = this.resources.getBasedir("reactor-graph");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .execute("clean", "depgraph:reactor");

    result.assertErrorFreeLog();

    assertFileContents(basedir, "expectations/reactor.gml", "target/dependency-graph.gml");
  }
}
