package com.github.ferstl.depgraph.graph.style.resource;

import org.junit.Test;
import com.github.ferstl.depgraph.graph.style.StyleConfiguration;
import static org.junit.Assert.assertTrue;


public class BuiltInStyleResourceTest {

  @Test
  public void resourcesExistAndCanBeLoaded() {
    for (BuiltInStyleResource resource : BuiltInStyleResource.values()) {
      ClasspathStyleResource styleResource = resource.createStyleResource(getClass().getClassLoader());

      assertTrue("Style configuration " + resource + " does not exist.", styleResource.exists());
      StyleConfiguration.load(styleResource);
    }
  }

}
