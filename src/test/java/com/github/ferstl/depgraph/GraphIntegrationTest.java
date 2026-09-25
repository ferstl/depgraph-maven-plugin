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
import java.nio.file.Path;
import org.junit.Assert;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.ferstl.depgraph.MavenVersion.MAX_VERSION;
import static com.github.ferstl.depgraph.MavenVersion.MIN_VERSION;
import static io.takari.maven.testing.TestResources.assertFileContents;
import static io.takari.maven.testing.TestResources.assertFilesNotPresent;
import static io.takari.maven.testing.TestResources.assertFilesPresent;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({MAX_VERSION, MIN_VERSION})
public class GraphIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;

  public GraphIntegrationTest(MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder
        .withCliOptions("-B")
        .build();
  }

  public void assertJsonFileContents(File basedir, String expFile, String srcFile) throws Exception {
    Path basePath = basedir.toPath();
    Path expFilePath = basePath.resolve(expFile);
    Path srcFilePath = basePath.resolve(srcFile);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode treeExp = mapper.readTree(expFilePath.toFile());
    JsonNode treeSrc = mapper.readTree(srcFilePath.toFile());

    Assert.assertEquals(treeExp, treeSrc);
  }

  @Before
  public void before() {
    // Workaround for https://github.com/takari/takari-plugin-testing-project/issues/14
    FileSystems.getDefault();
  }

  @Test
  public void graphInDot() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "sub-parent/module-3/target/dependency-graph.dot",
        "target/dependency-graph.dot",
        "sub-parent/target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/graph_parent.dot", "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-1.dot", "module-1/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-2.dot", "module-2/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_sub-parent.dot", "sub-parent/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_module-3.dot", "sub-parent/module-3/target/dependency-graph.dot");
  }

  @Test
  public void byGroupIdInDot() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "depgraph:by-groupid");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.dot",
        "module-2/target/dependency-graph.dot",
        "sub-parent/module-3/target/dependency-graph.dot",
        "target/dependency-graph.dot",
        "sub-parent/target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/by-groupid_parent.dot", "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-1.dot", "module-1/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-2.dot", "module-2/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_sub-parent.dot", "sub-parent/target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/by-groupid_module-3.dot", "sub-parent/module-3/target/dependency-graph.dot");
  }

  @Test
  public void exampleInDot() throws Exception {
    File basedir = this.resources.getBasedir("no-dependencies");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .execute("clean", "depgraph:example");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/example.dot", "target/dependency-graph.dot");
  }

  @Test
  public void customGraphStyle() throws Exception {
    File basedir = this.resources.getBasedir("single-dependency");
    String styleConfiguration = basedir.toPath().resolve("graph-style.json").toAbsolutePath().toString();

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DcustomStyleConfiguration=" + styleConfiguration)
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_custom-graph-style.dot", "target/dependency-graph.dot");
  }

  @Test
  public void graphMergeTypes() throws Exception {
    File basedir = this.resources.getBasedir("single-dependency");

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DmergeTypes")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");
    assertFileContents(basedir, "expectations/graph_without-types.dot", "target/dependency-graph.dot");
  }

  @Test
  public void aggregateWithoutDependencies() throws Exception {
    File basedir = this.resources.getBasedir("no-dependencies");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/aggregate-without-dependencies.gml", "target/dependency-graph.gml");
  }

  @Test
  public void targetIncludes() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .withCliOption("-DtargetIncludes=*:guava")
        .withCliOption("-DshowDuplicates")
        .withCliOption("-DshowConflicts")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.gml",
        "module-2/target/dependency-graph.gml",
        "sub-parent/module-3/target/dependency-graph.gml",
        "target/dependency-graph.gml",
        "sub-parent/target/dependency-graph.gml");

    assertFileContents(basedir, "expectations/graph_target-includes_parent.gml", "target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_target-includes_module-1.gml", "module-1/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_target-includes_module-2.gml", "module-2/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_target-includes_sub-parent.gml", "sub-parent/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_target-includes_module-3.gml", "sub-parent/module-3/target/dependency-graph.gml");
  }

  @Test
  public void transitiveIncludes() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DtransitiveIncludes=com.querydsl:*")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.txt",
        "module-2/target/dependency-graph.txt",
        "sub-parent/module-3/target/dependency-graph.txt",
        "target/dependency-graph.txt",
        "sub-parent/target/dependency-graph.txt");

    assertFileContents(basedir, "expectations/graph_transitive-includes_parent.txt", "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_transitive-includes_module-1.txt", "module-1/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_transitive-includes_module-2.txt", "module-2/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_transitive-includes_sub-parent.txt", "sub-parent/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_transitive-includes_module-3.txt", "sub-parent/module-3/target/dependency-graph.txt");
  }

  @Test
  public void transitiveExcludes() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DshowGroupIds")
        .withCliOption("-DtransitiveExcludes=com.google.*:*")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.txt");

    assertFileContents(basedir, "expectations/aggregate_transitive-excludes.txt", "target/dependency-graph.txt");
  }

  @Test
  public void transitiveAndTargetFiltering() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DshowGroupIds")
        .withCliOption("-DtargetIncludes=com.querydsl:*")
        .withCliOption("-DtransitiveExcludes=com.querydsl:*")
        .execute("clean", "depgraph:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.txt");

    assertFileContents(basedir, "expectations/aggregate_transitive-and-target-filtering.txt", "target/dependency-graph.txt");
  }

  @Test
  public void graphInGml() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=gml")
        .withCliOption("-DshowGroupIds")
        .withCliOption("-DshowVersions")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.gml",
        "module-2/target/dependency-graph.gml",
        "sub-parent/module-3/target/dependency-graph.gml",
        "target/dependency-graph.gml",
        "sub-parent/target/dependency-graph.gml");

    assertFileContents(basedir, "expectations/graph_parent.gml", "target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-1.gml", "module-1/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-2.gml", "module-2/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_sub-parent.gml", "sub-parent/target/dependency-graph.gml");
    assertFileContents(basedir, "expectations/graph_module-3.gml", "sub-parent/module-3/target/dependency-graph.gml");
  }

  @Test
  public void graphInJson() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=json")
        .withCliOption("-DshowDuplicates")
        .withCliOption("-DshowConflicts")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.json",
        "module-2/target/dependency-graph.json",
        "sub-parent/module-3/target/dependency-graph.json",
        "target/dependency-graph.json",
        "sub-parent/target/dependency-graph.json");

    assertJsonFileContents(basedir, "expectations/graph_parent.json", "target/dependency-graph.json");
    assertJsonFileContents(basedir, "expectations/graph_module-1.json", "module-1/target/dependency-graph.json");
    assertJsonFileContents(basedir, "expectations/graph_module-2.json", "module-2/target/dependency-graph.json");
    assertJsonFileContents(basedir, "expectations/graph_sub-parent.json", "sub-parent/target/dependency-graph.json");
    assertJsonFileContents(basedir, "expectations/graph_module-3.json", "sub-parent/module-3/target/dependency-graph.json");
  }

  @Test
  public void graphInText() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DgraphFormat=text")
        .withCliOption("-DshowVersions")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(
        basedir,
        "module-1/target/dependency-graph.txt",
        "module-2/target/dependency-graph.txt",
        "sub-parent/module-3/target/dependency-graph.txt",
        "target/dependency-graph.txt",
        "sub-parent/target/dependency-graph.txt");

    assertFileContents(basedir, "expectations/graph_parent.txt", "target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_module-1.txt", "module-1/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_module-2.txt", "module-2/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_sub-parent.txt", "sub-parent/target/dependency-graph.txt");
    assertFileContents(basedir, "expectations/graph_module-3.txt", "sub-parent/module-3/target/dependency-graph.txt");
  }

  @Test
  public void outputLocationParameters() throws Exception {
    File basedir = this.resources.getBasedir("single-dependency");

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DoutputDirectory=" + basedir.toPath().toAbsolutePath().toString() + "/target/custom-directory")
        .withCliOption("-DoutputFileName=custom-graph")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/custom-directory/custom-graph.dot");
  }

  @Test
  public void useArtifactIdInFileName() throws Exception {
    File basedir = this.resources.getBasedir("single-dependency");

    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DuseArtifactIdInFileName")
        .withCliOption("-DoutputFileName=not-relevant")
        .execute("clean", "depgraph:graph");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/single-dependency.dot");
  }

  @Test
  public void aggregateWithMultipleParents() throws Exception {
    File basedir = this.resources.getBasedir("multiple-parents");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-DincludeParentProjects")
        // Somehow this test does not work on travis without fully qualifying the plugin.
        .execute("clean", "com.github.ferstl:depgraph-maven-plugin:aggregate");

    result.assertErrorFreeLog();
    assertFilesPresent(basedir, "target/dependency-graph.dot");

    assertFileContents(basedir, "expectations/aggregate-multiple-parents.dot", "target/dependency-graph.dot");
  }

  @Test
  public void skipExecution() throws Exception {
    File basedir = this.resources.getBasedir("depgraph-maven-plugin-test");
    MavenExecutionResult result = this.mavenRuntime
        .forProject(basedir)
        .withCliOption("-Ddepgraph.skip")
        // Somehow this test does not work on travis without fully qualifying the plugin.
        .execute("clean", "com.github.ferstl:depgraph-maven-plugin:aggregate");

    result.assertErrorFreeLog();
    result.assertLogText("Skipping execution");

    assertFilesNotPresent(basedir, "target/dependency-graph.dot");
  }
}
