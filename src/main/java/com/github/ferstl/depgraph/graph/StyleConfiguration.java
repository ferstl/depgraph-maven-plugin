package com.github.ferstl.depgraph.graph;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import com.google.common.collect.ImmutableMap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StyleConfiguration {

  NodeConfiguration defaultNodeConfiguration = new NodeConfiguration();
  Map<String, NodeConfiguration> scopeNodeConfiguration = ImmutableMap.of("compile", new NodeConfiguration(), "test", new NodeConfiguration());

  static class NodeConfiguration {

    String shape = "polygon";
    int sides = 4;
    String color = "red";
    Font font = new Font();

  }

  static class Font {

    String color = "black";
    int size = 14;
    String name = "Helvetica";
  }
}
