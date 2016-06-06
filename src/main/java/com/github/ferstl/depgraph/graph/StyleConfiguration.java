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
  EdgeConfiguration defaultEdgeConfiguration = new EdgeConfiguration();
  Map<String, NodeConfiguration> scopeNodeConfiguration = ImmutableMap.of("compile", new NodeConfiguration(), "test", new NodeConfiguration());
  Map<NodeResolution, EdgeConfiguration> edgeTypeConfiguration = ImmutableMap.of(NodeResolution.INCLUDED, new EdgeConfiguration(), NodeResolution.OMITTED_FOR_DUPLICATE, new EdgeConfiguration());


  public static void main(String[] args) {
    createGson();
    // createYaml();
  }

  private static void createGson() {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
        .setPrettyPrinting()
        .create();

    // NodeConfiguration[] c = new NodeConfiguration[]{new NodeConfiguration(), new NodeConfiguration(), new
    // NodeConfiguration()};
    // System.out.println(gson.toJson(c));
    System.out.println(gson.toJson(new StyleConfiguration()));
  }

  private static void createYaml() {
    DumperOptions options = new DumperOptions();

    Yaml yaml = new Yaml(options);
    yaml.setBeanAccess(BeanAccess.FIELD);
    System.out.println(yaml.dumpAsMap(new StyleConfiguration()));
  }


  static class NodeConfiguration {

    String shape = "polygon";
    int sides = 4;
    String color = "red";
    Font font = new Font();
  }

  static class EdgeConfiguration {

    String style = "dotted";
    String color = "black";
    Font font = new Font();
  }

  static class Font {

    String color = "black";
    int size = 14;
    String name = "Helvetica";
  }
}
