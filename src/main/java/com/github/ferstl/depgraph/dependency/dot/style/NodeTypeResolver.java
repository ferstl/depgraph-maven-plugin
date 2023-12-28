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
package com.github.ferstl.depgraph.dependency.dot.style;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.SimpleType;

class NodeTypeResolver extends TypeIdResolverBase {

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
    return idFromValueAndType(value, value != null ? value.getClass() : Box.class);
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return suggestedType.getSimpleName().toLowerCase();
  }

  @Override
  public Id getMechanism() {
    return Id.CUSTOM;
  }

}
