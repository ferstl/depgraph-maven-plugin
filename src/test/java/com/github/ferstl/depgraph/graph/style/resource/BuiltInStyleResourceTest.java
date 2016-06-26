package com.github.ferstl.depgraph.graph.style.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BuiltInStyleResourceTest {

  private final BuiltInStyleResource resource;

  public BuiltInStyleResourceTest(BuiltInStyleResource resource) {
    this.resource = resource;
  }

  @Parameters(name = "{0}")
  public static BuiltInStyleResource[] resources() {
    return BuiltInStyleResource.values();
  }

  @Test
  public void resourcesExistAndCanBeLoaded() {
    ClasspathStyleResource styleResource = this.resource.createStyleResource(getClass().getClassLoader());

    assertTrue("Style configuration " + this.resource + " does not exist.", styleResource.exists());
    StyleConfiguration.load(styleResource);
  }

}
