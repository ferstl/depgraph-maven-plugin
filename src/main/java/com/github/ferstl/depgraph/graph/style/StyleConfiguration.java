package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.ferstl.depgraph.dot.AttributeBuilder;
import com.github.ferstl.depgraph.graph.NodeResolution;
import com.github.ferstl.depgraph.graph.style.resource.StyleResource;

public class StyleConfiguration {

  private final AbstractNode defaultNode = new Box();
  private final Edge defaultEdge = new Edge();
  private final Map<String, ? extends AbstractNode> scopeStyles = new LinkedHashMap<>();
  private final Map<NodeResolution, Edge> edgeResolutionStyles = new LinkedHashMap<>();


  public static StyleConfiguration load(StyleResource mainConfig, StyleResource... overrides) {
    SimpleModule module = new SimpleModule()
        .addKeySerializer(NodeResolution.class, new NodeResolutionSerializer())
        .addDeserializer(NodeResolution.class, new NodeResolutionDeserializer());

    ObjectMapper mapper = new ObjectMapper()
        .registerModule(module)
        .setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE)
        .setSerializationInclusion(Include.NON_EMPTY)
        .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);


    StyleConfiguration styleConfiguration = readConfig(mapper.readerFor(StyleConfiguration.class), mainConfig);
    for (StyleResource override : overrides) {
      styleConfiguration = readConfig(mapper.readerForUpdating(styleConfiguration), override);
    }

    return styleConfiguration;
  }

  private static StyleConfiguration readConfig(ObjectReader reader, StyleResource config) {
    try (InputStream is = config.openStream()) {
      return reader.readValue(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public AttributeBuilder defaultNodeAttributes() {
    return this.defaultNode.createAttributes();
  }

  public AttributeBuilder defaultEdgeAttributes() {
    return this.defaultEdge.createAttributes();
  }

  public AttributeBuilder edgeAttributes(NodeResolution resolution) {
    Edge edge = this.edgeResolutionStyles.get(resolution);
    return edge != null ? edge.createAttributes() : new AttributeBuilder();
  }

  public AttributeBuilder nodeAttributes(String groupId, String artifactId, String version, String scopes, String effectiveScope) {
    AbstractNode node = this.scopeStyles.containsKey(effectiveScope) ? this.scopeStyles.get(effectiveScope) : this.defaultNode;
    return node.createAttributes(groupId, artifactId, version, scopes, node != this.defaultNode);
  }

}
