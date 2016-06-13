package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.graph.NodeResolution;
import com.google.common.collect.ImmutableMap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StyleConfiguration {

  AbstractNode defaultNode;
  Edge defaultEdge = new Edge();
  Map<String, ? extends AbstractNode> scopedNodes = ImmutableMap.of("compile", new Box(), "test", new Box());
  Map<NodeResolution, Edge> edgeTypes = ImmutableMap.of(NodeResolution.INCLUDED, new Edge(), NodeResolution.OMITTED_FOR_DUPLICATE, new Edge());

  public StyleConfiguration() {
    this.defaultNode = new Box();
    this.defaultNode.style = "rounded";
    this.defaultNode.defaultFont = new Font();
    this.defaultNode.defaultFont.name = "Helvetica";
    this.defaultNode.defaultFont.size = 14;
    this.defaultNode.groupIdFont = new Font();
    this.defaultNode.groupIdFont.size = 12;
    this.defaultNode.scopeFont = new Font();
    this.defaultNode.scopeFont.size = 12;

    this.defaultEdge = new Edge();
    this.defaultEdge.font = new Font();
    this.defaultEdge.font.name = "Helvetica";
    this.defaultEdge.font.size = 10;
  }

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
        .registerTypeAdapter(AbstractNode.class, new JsonDeserializer<AbstractNode>() {

          @Override
          public AbstractNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonElement type = json.getAsJsonObject().get("type");
            if (type == null) {
              throw new NullPointerException("Undefined shape type");
            }

            switch (type.getAsString()) {
              case "box":
                return context.deserialize(json, Box.class);
              case "polygon":
                return context.deserialize(json, Polygon.class);
              case "ellipse":
                return context.deserialize(json, Ellipse.class);
              default:
                throw new IllegalArgumentException("Unknown shape type: " + type.getAsString());
            }
          }
        })
        .registerTypeAdapter(AbstractNode.class, new JsonSerializer<AbstractNode>() {

          @Override
          public JsonElement serialize(AbstractNode src, Type typeOfSrc, JsonSerializationContext context) {
            if (src instanceof Box) {
              return context.serialize(src, Box.class);
            } else if (src instanceof Polygon) {
              return context.serialize(src, Polygon.class);
            } else if (src instanceof Ellipse) {
              return context.serialize(src, Ellipse.class);
            } else {
              throw new IllegalArgumentException("Unsupported shape type: " + typeOfSrc);
            }
          }
        })
        .setPrettyPrinting()
        .create();

    System.out.println(gson.toJson(new StyleConfiguration()));

    try (InputStream is = ClassLoader.getSystemResourceAsStream("style.json")) {
      StyleConfiguration config = gson.fromJson(new InputStreamReader(is), StyleConfiguration.class);
      System.err.println(gson.toJson(config));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static class Edge {

    String style;
    String color;
    Font font;

    public void configureGlobally(AttributeBuilder builder) {
      builder
          .style(this.style)
          .color(this.color);

      this.font.configureGlobally(builder);
    }
  }

  static class Font {

    String color;
    Integer size;
    String name;

    public void configureGlobally(AttributeBuilder builder) {
      builder
          .color(this.color)
          .fontSize(this.size)
          .fontName(this.name);
    }
  }

  abstract static class AbstractNode {

    final String type;
    String color;
    String style;
    Font defaultFont;
    Font groupIdFont;
    Font artifactIdFont;
    Font versionFont;
    Font scopeFont;

    AbstractNode(String type) {
      this.type = type;
    }

    public final void configureGlobally(AttributeBuilder builder) {
      builder.shape(this.type)
          .style(this.style)
          .color(this.color)
          .fontName(this.defaultFont.name)
          .fontSize(this.defaultFont.size)
          .fontColor(this.defaultFont.color);
    }

  }

  static class Box extends AbstractNode {

    Box() {
      super("box");
    }
  }

  static class Polygon extends AbstractNode {

    int sides = 4;

    Polygon() {
      super("polygon");
    }

  }

  static class Ellipse extends AbstractNode {

    Ellipse() {
      super("ellipse");
    }
  }
}
