/*
 * Copyright (c) 2014 by Stefan Ferstl <st.ferstl@gmail.com>
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
package org.apache.maven.artifact.resolver;

import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

/**
 * Dummy interface that must be on the class path when mocking {@link DependencyTreeBuilder}.
 * It is used in a deprecated method in {@link DependencyTreeBuilder}.
 */
public interface ArtifactCollector {

}
