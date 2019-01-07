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
package com.github.ferstl.depgraph.dependency;

import java.util.Collection;
import org.apache.maven.project.MavenProject;

/**
 * Functional interface to supply sub projects for a parent project. This is only used
 * to avoid injecting Maven infrastructure into the graph factory.
 */
// TODO Remove this after Java8 migration
public interface SubProjectSupplier {

  Collection<MavenProject> getSubProjects(MavenProject parent);
}
