package com.github.ferstl.depgraph;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import com.github.ferstl.depgraph.dependency.GraphFactory;
import com.github.ferstl.depgraph.dependency.GraphStyleConfigurer;
import com.github.ferstl.depgraph.dependency.dot.style.resource.BuiltInStyleResource;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

public class AbstractGraphMojoTest {

    private AbstractGraphMojo mojo;
    private MavenProject project;

    @Before
    public void setUp() {
        // Initialize the test environment by creating an instance of AbstractGraphMojo and MavenProject
        mojo = new AbstractGraphMojo() {
            @Override
            protected GraphFactory createGraphFactory(GraphStyleConfigurer graphStyleConfigurer) {
                return null;
            }

            @Override
            protected Set<BuiltInStyleResource> getAdditionalStyleResources() {
                return null;
            }
        };

        project = new MavenProject();
        mojo.project = project;
    }

    @Test
    public void testCreateGraphFilePath_OutputDirectoryResolved() throws Exception {
        // Test case to verify the behavior of creating a graph file path when the output directory is resolved

        // Set up the required parameters for testing
        mojo.useArtifactIdInFileName = false;
        mojo.graphFormat = "dot";
        mojo.outputFileName = "outputFile";
        mojo.outputDirectory = new File("target");

        // Create the graph file path
        Path graphFilePath = mojo.createGraphFilePath(GraphFormat.DOT);

        // Ensure that the graph file path is correctly constructed
        assertEquals("target/outputFile.dot", graphFilePath.toString());
    }

    @Test
    public void testCreateGraphFilePath_OutputDirectoryNotResolved() throws Exception {
        // Test case to verify the behavior of creating a graph file path when the output directory is not resolved

        // Set up the required parameters for testing
        mojo.useArtifactIdInFileName = false;
        mojo.graphFormat = "dot";
        mojo.outputFileName = "outputFile";
        mojo.outputDirectory = new File("${project.basedir}");

        // Create the graph file path, which should use the current directory since project.basedir is not resolved
        Path graphFilePath = mojo.createGraphFilePath(GraphFormat.DOT);

        // Ensure that the graph file path is constructed using the current directory
        assertEquals(new File("outputFile.dot").toPath().toAbsolutePath().toString(), graphFilePath.toString());
    }

    @Test
    public void testAddFileExtensionIfNeeded_WithExtension() throws Exception {
        // Test case to verify the behavior of adding a file extension when it already exists

        String fileName = "outputFile.dot";
        String result = mojo.addFileExtensionIfNeeded(GraphFormat.DOT, fileName);

        // Ensure that the result remains the same since the extension is already present
        assertEquals(fileName, result);
    }

    @Test
    public void testAddFileExtensionIfNeeded_WithoutExtension() throws Exception {
        // Test case to verify the behavior of adding a file extension when it is missing

        String fileName = "outputFile";
        String result = mojo.addFileExtensionIfNeeded(GraphFormat.DOT, fileName);

        // Ensure that the result has the ".dot" extension added
        assertEquals("outputFile.dot", result);
    }

    @Test
    public void testDetermineDotExecutable_Default() throws IOException {
        // Test case to verify the determination of the default Dot executable

        // Determine the Dot executable
        String dotExecutable = mojo.determineDotExecutable();

        // Ensure that the default Dot executable is correctly determined
        assertEquals("dot", dotExecutable);
    }
}