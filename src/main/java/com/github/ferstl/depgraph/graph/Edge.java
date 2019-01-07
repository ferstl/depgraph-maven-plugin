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
package com.github.ferstl.depgraph.graph;

import java.util.Objects;

public final class Edge {

  private final String fromNodeId;
  private final String toNodeId;
  private final String name;
  // Not part of equals()/hashCode()
  private final boolean permanent;

  public Edge(String fromNodeId, String toNodeId, String name) {
    this(fromNodeId, toNodeId, name, false);
  }

  public Edge(String fromNodeId, String toNodeId, String name, boolean permanent) {
    this.fromNodeId = fromNodeId;
    this.toNodeId = toNodeId;
    this.name = name;
    this.permanent = permanent;
  }

  public String getFromNodeId() {
    return this.fromNodeId;
  }

  public String getToNodeId() {
    return this.toNodeId;
  }

  public String getName() {
    return this.name;
  }

  public boolean isPermanent() {
    return this.permanent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!(o instanceof Edge)) { return false; }

    Edge edge = (Edge) o;
    return Objects.equals(this.fromNodeId, edge.fromNodeId)
        && Objects.equals(this.toNodeId, edge.toNodeId)
        && Objects.equals(this.name, edge.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fromNodeId, this.toNodeId, this.name);
  }

  @Override
  public String toString() {
    return this.fromNodeId + " -> " + this.toNodeId + " (" + this.name + ")";
  }
}
