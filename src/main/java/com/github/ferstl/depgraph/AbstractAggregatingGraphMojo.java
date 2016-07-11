/*
 * Copyright (c) 2014 - 2016 by Stefan Ferstl <st.ferstl@gmail.com>
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
package com.github.ferstl.depgraph;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractAggregatingGraphMojo extends AbstractGraphMojo {

  /**
   * Merge dependencies that occur in multiple scopes into one graph node instead of having a node per scope.
   *
   * @since 2.0.0
   */
  @Parameter(property = "mergeScopes", defaultValue = "false")
  boolean mergeScopes;

}
