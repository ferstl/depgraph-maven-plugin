package com.github.ferstl.depgraph;

import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class ExampleDependencyGraphMojoTest {

    private ExampleDependencyGraphMojo mojo;

    @Before
    public void setUp() {
        // Set up a new instance of ExampleDependencyGraphMojo for testing
        mojo = new ExampleDependencyGraphMojo();
    }

    @Test
    public void testExampleGraphFactory() throws NoSuchFieldException, IllegalAccessException {
        // Create an instance of ExampleGraphFactory with null arguments
        ExampleDependencyGraphMojo.ExampleGraphFactory factory = new ExampleDependencyGraphMojo.ExampleGraphFactory(null, null, null);

        // Ensure that the factory instance is not null
        assertNotNull(factory);

        // Access private fields using reflection and test them

        // Get a reference to the private "globalFilter" field in ExampleGraphFactory
        Field globalFilterField = ExampleDependencyGraphMojo.ExampleGraphFactory.class.getDeclaredField("globalFilter");
        // Get a reference to the private "targetFilter" field in ExampleGraphFactory
        Field targetFilterField = ExampleDependencyGraphMojo.ExampleGraphFactory.class.getDeclaredField("targetFilter");

        // Make the private fields accessible for testing
        globalFilterField.setAccessible(true);
        targetFilterField.setAccessible(true);

        // Ensure that both globalFilter and targetFilter are null
        assertNull(globalFilterField.get(factory));
        assertNull(targetFilterField.get(factory));
    }
}
