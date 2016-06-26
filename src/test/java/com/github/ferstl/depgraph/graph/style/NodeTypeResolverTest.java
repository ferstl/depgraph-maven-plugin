package com.github.ferstl.depgraph.graph.style;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import static org.junit.Assert.assertEquals;


public class NodeTypeResolverTest {

  private NodeTypeResolver resolver;

  @Before
  public void before() {
    this.resolver = new NodeTypeResolver();
  }

  @Test
  public void typeFromId() {
    assertEquals(Box.class, this.resolver.typeFromId(null, "box").getRawClass());
    assertEquals(Polygon.class, this.resolver.typeFromId(null, "polygon").getRawClass());
    assertEquals(Ellipse.class, this.resolver.typeFromId(null, "ellipse").getRawClass());
  }

  @Test(expected = RuntimeException.class)
  public void typeFromIdForUnknownId() {
    assertEquals(Ellipse.class, this.resolver.typeFromId(null, "moebius").getRawClass());
  }

  @Test
  public void getIdFromValue() {
    assertEquals("box", this.resolver.idFromValue(new Box()));
    assertEquals("polygon", this.resolver.idFromValue(new Polygon()));
    assertEquals("ellipse", this.resolver.idFromValue(new Ellipse()));
  }

  @Test
  public void getIdFromValueAndType() {
    assertEquals("box", this.resolver.idFromValueAndType(null, Box.class));
    assertEquals("polygon", this.resolver.idFromValueAndType(null, Polygon.class));
    assertEquals("ellipse", this.resolver.idFromValueAndType(null, Ellipse.class));
  }

  @Test
  public void getMechanism() {
    assertEquals(Id.CUSTOM, this.resolver.getMechanism());
  }
}
