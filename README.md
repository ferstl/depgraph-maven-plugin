# depgraph-maven-plugin [![Build Status](https://travis-ci.org/ferstl/depgraph-maven-plugin.svg?branch=master)](https://travis-ci.org/ferstl/depgraph-maven-plugin)
*- A Maven plugin that generates dependency graphs*

This Maven plugin generates dependency graphs on single modules or in an aggregated form on multi-module projects. The graphs are represented by `.dot` files. In case that [Graphviz](http://www.graphviz.org/) is installed on the machine where this plugin is run, the `.dot` file can be directly converted into all supported image files.


For more information take a look at the generated [plugin page](https://ferstl.github.io/depgraph-maven-plugin/index.html).


## Plugin Coordinates

The *depgraph-maven-plugin* is available on [Maven Central](http://central.maven.org/maven2/com/github/ferstl/depgraph-maven-plugin/). So no further repository configuration is required.

These are the plugin coordinates:

    <build>
      <plugins>
        <plugin>
          <groupId>com.github.ferstl</groupId>
          <artifactId>depgraph-maven-plugin</artifactId>
          <version>1.0.1</version>
        </plugin>
      </plugins>
    </build>
    
## Release Notes

- Version 1.0.0 (first version)
- [Version 1.0.1](https://github.com/ferstl/depgraph-maven-plugin/issues?q=milestone%3A%22Version+1.0.1%22+is%3Aclosed)

## Examples

All examples are based on a multi-module project with the following structure:

    parent
    - module-1
    - module-2
    - sub-parent
      - module-3

Each of the modules contains some dependencies in different scopes.


### Simple Dependency Graph

A simple graph can be created by executing the [`depgraph:graph`](https://ferstl.github.io/depgraph-maven-plugin/graph-mojo.html) goal:

<img src="https://raw.githubusercontent.com/ferstl/depgraph-maven-plugin/gh-pages/images/documentation/simple-graph.png" alt="Simple dependency graph"/>

The goal can be configured to show the versions on the dependencies:

<img src="https://raw.githubusercontent.com/ferstl/depgraph-maven-plugin/gh-pages/images/documentation/with-versions.png" alt="Simple dependency graph with versions"/>

### Duplicates and Conflicts

The [`depgraph:graph`](https://ferstl.github.io/depgraph-maven-plugin/graph-mojo.html) goal can be configured to show duplicate and/or conflicting versions. Duplicate versions are shown as dotted black arrows. Conflicting versions are shown as dashed red arrows:

<img src="https://raw.githubusercontent.com/ferstl/depgraph-maven-plugin/gh-pages/images/documentation/duplicates-and-conflicts.png" alt="Dependency graph showing duplicates and conflicts"/>

Duplicate dependencies do occur when more than one module defines the same dependency, which is not a problem. Maven's dependency resolution will just pick one dependency and omit all the duplicates. A conflict occurs when the same dependency occurs in different versions in the reactor. In this case Maven will choose the [nearest](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) version and ommit the others. Occurrences of conflicting versions should be investigated and solved if possible.  

### Dependency Graph by `groupId`

The [`depgraph:by-groupid`](https://ferstl.github.io/depgraph-maven-plugin/by-groupid-mojo.html) goal creates a dependency graph using the `groupId` of the dependencies:

<img src="https://raw.githubusercontent.com/ferstl/depgraph-maven-plugin/gh-pages/images/documentation/by-group-id.png" alt="Dependency graph by groupId"/>

Such graphs give a higher-level overview of a project, i.e. they show better which "frameworks" used by a maven project.


### Aggregated Graphs

The goal [`depgraph:aggregate`](https://ferstl.github.io/depgraph-maven-plugin/aggregate-mojo.html) creates an aggregated dependency graph on the root of a multi-module project. It shows the sub-modules with dotted black arrows and the **union** of all the modules' dependencies (the `sub-parent` and its `module-3` were excluded from the graph for clarity):

<img src="https://raw.githubusercontent.com/ferstl/depgraph-maven-plugin/gh-pages/images/documentation/aggregated.png" alt="Aggregated ependency graph"/>


The goal [`depgraph:aggregate-by-groupid`](https://ferstl.github.io/depgraph-maven-plugin/aggregate-by-groupid-mojo.html) does the same for the group IDs of all modules and their dependencies.


## FAQ

Q: Help! The dependency graph of my 10 year old 100-module enterprise project looks like a ball of wool. I can't see anything!

A: Think carefully what information you want to see in your dependency graph. Do you really want to have all third-party dependencies in your graph or do you want to see only the dependencies between your own modules? Would the `groupId` graph be a better alternative?
Generally, you should consequently exclude dependencies that don't give you useful information. The inclusion/exclusion mechanisms in this plugin are quite powerful and easy to use. A good starting point is the exclusion of dependencies to "utility" libraries, such as `commons-lang`, `guava` or `my-enterprise-project-common`. Such dependencies are typically used by every single module which is (in most cases) perfectly fine. So they don't give you much useful information when they show up in the dependency graph.

-----

Q: Why can't I show duplicates and conflicts in the aggregated graph.

A: This does not make sense because dependencies are resolved individually for each module in the reactor. As a result, the same dependency edge in the graph could once occur as conflict, once as duplicate and once as resolved dependency.

Example:
Suppose, we have two modules `module-1` and `module-2` in the reactor. `module-2` has a dependency to `module-1` and both modules have a dependency to the third-party library `commons-wtf`. `module-1` references version `3.0` of this library and `module-2` references version `3.1`. If we now would display conflicts in the aggregated graph, we would get these edges (amongst others):

    module-1 --- 3.1 (included) ---> commons-wtf  (perspective of module-1)
    module-1 --- 3.1 (conflict) ---> commons-wtf  (perspective of module-2)
    module-2 --- 3.0 (included) ---> commons-wtf  (perspective of module-2)

Such a graph is not useful whatsoever. Unless it is not possible to access all resolved dependencies within the whole reactor, we cannot show duplicates and conflicts.
