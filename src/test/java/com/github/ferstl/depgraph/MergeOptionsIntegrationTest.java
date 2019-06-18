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
public class MergeOptionsIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public MergeOptionsIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder.build();
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void graphNoMerge() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_module-1_no-merge.dot", "module-1/target/dependency-graph.dot");
  }

  @Test
  public void aggregateNoMerge() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_no-merge.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateByGroupIdNoMerge() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate-by-groupid");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate-by-groupid_no-merge.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateMergeScopes() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeScopes")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_mergeScopes.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateByGroupIdMergeScopes() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeScopes")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate-by-groupid");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate-by-groupid_mergeScopes.dot", "target/dependency-graph.dot");
  }

  @Test
  public void graphMergeClassifiers() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeClassifiers")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_module-1_mergeClassifiers.dot", "module-1/target/dependency-graph.dot");
  }

  @Test
  public void graphMergeTypes() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_module-1_mergeTypes.dot", "module-1/target/dependency-graph.dot");
  }

  @Test
  public void graphMergeTypesAndClassifiers() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        .withCliOption("-DmergeClassifiers")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_module-1_mergeTypes-mergeClassifiers.dot", "module-1/target/dependency-graph.dot");
  }

  @Test
  public void aggregateMergeClassifiers() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeClassifiers")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_mergeClassifiers.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateMergeTypes() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_mergeTypes.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateMergeTypesAndClassifiers() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        .withCliOption("-DmergeClassifiers")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_mergeTypes-mergeClassifiers.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateMergeTypesAndClassifiersAndScopes() throws Exception {
    File basedir = this.resources.getBasedir("merge-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        .withCliOption("-DmergeClassifiers")
        .withCliOption("-DmergeScopes")
        // Maven 3.1 does not resolve the module-2 ZIP dependency from the reactor. That's why we need to execute "install".
        .execute("clean", "install", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate_mergeTypes-mergeClassifiers-mergeScopes.dot", "target/dependency-graph.dot");
  }
}
