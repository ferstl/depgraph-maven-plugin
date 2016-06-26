package com.github.ferstl.depgraph.graph.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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

  AbstractNode defaultNode;
  Edge defaultEdge = new Edge();
  Map<String, ? extends AbstractNode> scopedNodes;
  Map<NodeResolution, Edge> edgeTypes;


  public static StyleConfiguration load(StyleResource mainConfig, StyleResource... overrides) {
    SimpleModule module = new SimpleModule()
        .addKeySerializer(NodeResolution.class, new NodeResolutionSerializer())
        .addDeserializer(NodeResolution.class, new NodeResolutionDeserializer());

    ObjectMapper mapper = new ObjectMapper()
        .registerModule(module)
        .setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE)
        .setSerializationInclusion(Include.NON_EMPTY)
        .setVisibility(PropertyAccessor.FIELD, Visibility.NON_PRIVATE);


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
    return getDefaultNode().createAttributes();
  }

  public AttributeBuilder defaultEdgeAttributes() {
    return getDefaultEdge().createAttributes();
  }

  public AttributeBuilder edgeAttributes(NodeResolution resolution) {
    Edge edge = getEdgeTypes().get(resolution);
    return edge != null ? edge.createAttributes() : new AttributeBuilder();
  }

  public AttributeBuilder nodeAttributes(String groupId, String artifactId, String version, String scopes, String effectiveScope) {
    Map<String, ? extends AbstractNode> scopedNodes = getScopedNodes();
    AbstractNode node = scopedNodes.containsKey(effectiveScope) ? scopedNodes.get(effectiveScope) : getDefaultNode();
    return node.createAttributes(groupId, artifactId, version, scopes);
  }

  private AbstractNode getDefaultNode() {
    return this.defaultNode != null ? this.defaultNode : new Box();
  }

  private Edge getDefaultEdge() {
    return this.defaultEdge != null ? this.defaultEdge : new Edge();
  }

  private Map<String, ? extends AbstractNode> getScopedNodes() {
    return this.scopedNodes != null ? this.scopedNodes : Collections.<String, AbstractNode>emptyMap();
  }

  private Map<NodeResolution, Edge> getEdgeTypes() {
    return this.edgeTypes != null ? this.edgeTypes : Collections.<NodeResolution, Edge>emptyMap();
  }

}
