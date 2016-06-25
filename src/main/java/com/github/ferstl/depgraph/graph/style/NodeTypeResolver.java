package com.github.ferstl.depgraph.graph.style;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.SimpleType;

public class NodeIdResolver extends TypeIdResolverBase {

  @Override
  public JavaType typeFromId(DatabindContext context, String id) {
    try {
      return SimpleType.constructUnsafe(Class.forName(getClass().getPackage().getName() + "." + id.substring(0, 1).toUpperCase() + id.substring(1)));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String idFromValue(Object value) {
    return idFromValueAndType(value, value.getClass());
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return value.getClass().getSimpleName().toLowerCase();
  }

  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }

}
