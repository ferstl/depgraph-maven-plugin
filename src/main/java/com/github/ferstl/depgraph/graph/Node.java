/*
 * Copyright (c) 2014 - 2018 the original author or authors.
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

public final class Node<T> {

  private final String nodeId;
  private final String nodeName;
  final T nodeObject;

  public Node(String nodeId, String nodeName, T nodeObject) {
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.nodeObject = nodeObject;
  }

  public String getNodeId() {
    return this.nodeId;
  }

  public String getNodeName() {
    return this.nodeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Node)) {
      return false;
    }

    Node<?> other = (Node<?>) o;
    return Objects.equals(this.nodeId, other.nodeId)
        && Objects.equals(this.nodeName, other.nodeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.nodeId, this.nodeName);
  }

  @Override
  public String toString() {
    return this.nodeId + "(" + this.nodeName + ")";
  }
}
