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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecution;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import static com.github.ferstl.depgraph.MavenVersion.MAX_VERSION;
import static io.takari.maven.testing.TestResources.assertFilesPresent;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static org.junit.Assume.assumeTrue;

/**
 * Integration test that creates the images for this plugin's README.MD documentation.
 */
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions(MAX_VERSION)
public class DocumentationIntegrationTest {

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;
  private File basedir;
  private Path imagePath;

  public DocumentationIntegrationTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    this.mavenRuntime = builder
        .withCliOptions("-B")
        .build();
  }

  @Before
  public void before() throws IOException {
    // Skip if graphviz is not installed
    assumeTrue(isGraphvizInstalled());

    this.basedir = getBaseDir();
    this.imagePath = createDirectories(Paths.get("target", "collected-images"));
  }

  @Test
  public void simpleGraph() throws Exception {
    runTest("graph");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");

    collectFile("sub-parent/module-3/target/dependency-graph.png", "simple-graph.png");
  }

  @Test
  public void withVersions() throws Exception {
    runTest("graph", "-DshowVersions");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");

    collectFile("sub-parent/module-3/target/dependency-graph.png", "with-versions.png");
  }

  @Test
  public void withGroupIds() throws Exception {
    runTest("graph", "-DshowGroupIds");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");

    collectFile("sub-parent/module-3/target/dependency-graph.png", "with-group-ids.png");
  }

  @Test
  public void withDuplicatesAndConflicts() throws Exception {
    runTest("graph",
        "-DshowVersions",
        "-DshowDuplicates",
        "-DshowConflicts");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.png",
        "module-2/target/dependency-graph.png",
        "sub-parent/module-3/target/dependency-graph.png",
        "target/dependency-graph.png",
        "sub-parent/target/dependency-graph.png");

    collectFile("sub-parent/module-3/target/dependency-graph.png", "duplicates-and-conflicts.png");
  }

  @Test
  public void aggregated() throws Exception {
    runTest("aggregate",
        "-DincludeParentProjects",
        "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3");

    assertFilesPresent(this.basedir, "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "aggregated.png");
  }

  @Test
  public void aggregatedWithDotArguments() throws Exception {
    runTest("aggregate",
        "-DincludeParentProjects",
        "-DdotArguments=-Kneato    -Nfontcolor=red",
        "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3");

    assertFilesPresent(this.basedir, "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "dot-arguments.png");
  }

  @Test
  public void aggregatedJson() throws Exception {
    runTest("aggregate",
        "-DgraphFormat=json",
        "-Dincludes=com.github.ferstl:*",
        "-Dexcludes=*:module-3");

    assertFilesPresent(this.basedir, "target/dependency-graph.json");

    collectFile("target/dependency-graph.json", "aggregated.json");
  }

  @Test
  public void aggregatedMermaid() throws Exception {
    runTest("aggregate",
            "-DgraphFormat=mermaid",
            "-DincludeParentProjects",
            "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3");

    assertFilesPresent(this.basedir, "target/dependency-graph.mmd");

    collectFile("target/dependency-graph.mmd", "aggregated.mmd");
  }

  @Test
  public void aggregatedText() throws Exception {
    runTest("aggregate",
        "-DgraphFormat=text",
        "-DincludeParentProjects",
        "-DclasspathScope=compile",
        "-DshowVersions");

    assertFilesPresent(this.basedir, "target/dependency-graph.txt");

    collectFile("target/dependency-graph.txt", "aggregated.txt");
  }

  @Test
  public void aggregatedByGroupId() throws Exception {
    runTest("aggregate-by-groupid");

    assertFilesPresent(this.basedir, "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "by-group-id.png");
  }

  @Test
  public void customStyle() throws Exception {
    String styleConfiguration = this.basedir.toPath().resolve("custom-style.json").toAbsolutePath().toString();
    runTest(
        "aggregate",
        "-DshowGroupIds",
        "-DincludeParentProjects",
        "-Dexcludes=com.github.ferstl:sub-parent,com.github.ferstl:module-3",
        "-DcustomStyleConfiguration=" + styleConfiguration);

    assertFilesPresent(this.basedir, "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "aggregated-styled.png");
  }

  @Test
  public void gmlWithConflicts() throws Exception {
    runTest("graph",
        "-DgraphFormat=gml",
        "-DshowVersions",
        "-DshowDuplicates",
        "-DshowConflicts");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.gml",
        "module-2/target/dependency-graph.gml",
        "sub-parent/module-3/target/dependency-graph.gml",
        "target/dependency-graph.gml",
        "sub-parent/target/dependency-graph.gml");

    collectFile("sub-parent/module-3/target/dependency-graph.gml", "with-conflicts.gml");
  }

  @Test
  public void mermaidWithConflicts() throws Exception {
    runTest("graph",
            "-DgraphFormat=mermaid",
            "-DshowVersions",
            "-DshowDuplicates",
            "-DshowConflicts");

    assertFilesPresent(
            this.basedir,
            "module-1/target/dependency-graph.mmd",
            "module-2/target/dependency-graph.mmd",
            "sub-parent/module-3/target/dependency-graph.mmd",
            "target/dependency-graph.mmd",
            "sub-parent/target/dependency-graph.mmd");

    collectFile("sub-parent/module-3/target/dependency-graph.mmd", "with-conflicts.mmd");
  }

  @Test
  public void pumlWithConflicts() throws Exception {
    runTest("graph",
        "-DgraphFormat=puml",
        "-DshowVersions",
        "-DshowDuplicates",
        "-DshowConflicts");

    assertFilesPresent(
        this.basedir,
        "module-1/target/dependency-graph.puml",
        "module-2/target/dependency-graph.puml",
        "sub-parent/module-3/target/dependency-graph.puml",
        "target/dependency-graph.puml",
        "sub-parent/target/dependency-graph.puml");

    collectFile("sub-parent/module-3/target/dependency-graph.puml", "with-conflicts.puml");
  }

  @Test
  public void forArtifact() throws Exception {
    runTest("for-artifact",
        "-DgraphFormat=dot",
        "-DexcludeOptionalDependencies",
        "-DgroupId=org.springframework",
        "-DartifactId=spring-jdbc",
        "-Dversion=5.1.3.RELEASE");

    assertFilesPresent(
        this.basedir,
        "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "for-artifact.png");
  }

  @Test
  public void reactor() throws Exception {
    this.basedir = getBaseDirForReactor();
    runTest("reactor",
        "-DgraphFormat=dot");

    assertFilesPresent(
        this.basedir,
        "target/dependency-graph.png");

    collectFile("target/dependency-graph.png", "reactor.png");
  }

  private void runTest(String goal, String... cliOptions) throws Exception {
    MavenExecution execution = this.mavenRuntime.forProject(this.basedir);
    execution.withCliOption("-DcreateImage");
    for (String cliOption : cliOptions) {
      execution.withCliOption(cliOption);
    }

    MavenExecutionResult result = execution.execute("clean", "depgraph:" + goal);
    result.assertErrorFreeLog();
  }

  private File getBaseDir() throws IOException {
    return this.resources.getBasedir("depgraph-maven-plugin-test");
  }

  private File getBaseDirForReactor() throws IOException {
    return this.resources.getBasedir("reactor-graph");
  }

  private void collectFile(String file, String renameTo) throws IOException {
    Path imageFile = this.basedir.toPath().resolve(file);
    Path targetFile = this.imagePath.resolve(renameTo);

    copy(imageFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
  }

  private static boolean isGraphvizInstalled() {
    return getDotExecutable() != null;
  }

  private static String getDotExecutable() {
    Commandline cmd = new Commandline();
    String finderExecutable = isWindows() ? "where.exe" : "which";

    cmd.setExecutable(finderExecutable);
    cmd.addArguments(new String[]{"dot"});

    CommandLineUtils.StringStreamConsumer systemOut = new CommandLineUtils.StringStreamConsumer();
    CommandLineUtils.StringStreamConsumer systemErr = new CommandLineUtils.StringStreamConsumer();

    try {
      int exitCode = CommandLineUtils.executeCommandLine(cmd, systemOut, systemErr);
      if (exitCode != 0) {
        return null;
      }
    } catch (CommandLineException e) {
      return null;
    }

    return systemOut.getOutput();
  }

  private static boolean isWindows() {
    String osName = System.getProperty("os.name", "n/a");
    return osName.toLowerCase(Locale.US).contains("windows");
  }
}
