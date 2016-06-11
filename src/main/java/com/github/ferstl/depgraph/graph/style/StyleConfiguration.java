package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import java.util.Map;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.dot.LabelBuilder;
import com.github.ferstl.depgraph.graph.NodeResolution;
import com.google.common.collect.ImmutableMap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StyleConfiguration {

  NodeConfiguration defaultNode = new NodeConfiguration();
  EdgeConfiguration defaultEdge = new EdgeConfiguration();
  Map<String, NodeConfiguration> scopedNodes = ImmutableMap.of("compile", new NodeConfiguration(), "test", new NodeConfiguration());
  Map<NodeResolution, EdgeConfiguration> edgeTypes = ImmutableMap.of(NodeResolution.INCLUDED, new EdgeConfiguration(), NodeResolution.OMITTED_FOR_DUPLICATE, new EdgeConfiguration());


  public static void main(String[] args) {
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(NodeResolution.class, new TypeAdapter<NodeResolution>() {

          @Override
          public void write(JsonWriter out, NodeResolution value) throws IOException {
            out.value(value.name().replace('_', '-').toLowerCase());
          }

          @Override
          public NodeResolution read(JsonReader in) throws IOException {
            String value = in.nextString();
            return NodeResolution.valueOf(value.replace('-', '_').toUpperCase());
          }
        })
        .setPrettyPrinting()
        .create();

    System.out.println(gson.toJson(new StyleConfiguration()));
  }

  static class NodeConfiguration {

    AbstractNode shape = new Polygon();
    String color = "red";
    Font font = new Font();

    public void configure(AttributeBuilder builder) {
      builder.color(this.color);
      this.font.configure(builder);
    }

    public void configureGlobally(AttributeBuilder builder) {
      builder.color(this.color);
      this.font.configureGlobally(builder);
    }
  }

  static class EdgeConfiguration {

    String style = "dotted";
    String color = "black";
    Font font = new Font();

    public void configure(AttributeBuilder builder) {
      builder
          .style(this.style)
          .color(this.color);

      this.font.configure(builder);
    }

    public void configureGlobally(AttributeBuilder builder) {
      builder
          .style(this.style)
          .color(this.color);

      this.font.configureGlobally(builder);
    }
  }

  static class Font {

    String color = "black";
    int size = 14;
    String name = "Helvetica";

    public void configure(AttributeBuilder builder) {
      new LabelBuilder().font().color(this.color).size(this.size).name(this.name);
      // FUCK!!!!
    }

    public void configureGlobally(AttributeBuilder builder) {
      builder
          .color(this.color)
          .fontSize(this.size)
          .fontName(this.name);
    }
  }

  abstract static class AbstractNode {

    final String type;
    String color = "black";
    String style = "rounded";
    Font defaultFont = new Font();
    Font groupIdFont = this.defaultFont;
    Font artifactIdFont;
    Font versionFont;
    Font scopeFont;

    AbstractNode(String type) {
      this.type = type;
    }

    public void configureGlobally(AttributeBuilder builder) {
      builder.shape(this.type)
          .style(this.style)
          .color(this.color)
          .fontName(this.defaultFont.name)
          .fontSize(this.defaultFont.size)
          .fontColor(this.defaultFont.color);
    }

    public void configure(AttributeBuilder builder) {

    }

  }

  static class Polygon extends AbstractNode {

    int sides = 4;


    Polygon() {
      super("polygon");
    }

    @Override
    public void configureGlobally(AttributeBuilder builder) {
      super.configureGlobally(builder);
    }

    @Override
    public void configure(AttributeBuilder builder) {
      super.configure(builder);
    }
  }

  static class Ellipse extends AbstractNode {

    Ellipse() {
      super("ellipse");
    }

    @Override
    public void configureGlobally(AttributeBuilder builder) {
      super.configureGlobally(builder);
    }

    @Override
    public void configure(AttributeBuilder builder) {
      super.configure(builder);
    }
  }
}
