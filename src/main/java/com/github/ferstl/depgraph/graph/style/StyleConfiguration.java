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

  private static final Box EMPTY_NODE = new Box();
  private static final Edge EMPTY_EDGE = new Edge();

  private AbstractNode defaultNode;
  private Edge defaultEdge;
  private Map<String, ? extends AbstractNode> scopeStyles;
  private Map<NodeResolution, Edge> edgeResolutionStyles;


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
    return node.createAttributes(groupId, artifactId, version, scopes, node != this.defaultNode && node != EMPTY_NODE);
  }

  private AbstractNode getDefaultNode() {
    return this.defaultNode != null ? this.defaultNode : EMPTY_NODE;
  }

  private Edge getDefaultEdge() {
    return this.defaultEdge != null ? this.defaultEdge : EMPTY_EDGE;
  }

  private Map<String, ? extends AbstractNode> getScopedNodes() {
    return this.scopeStyles != null ? this.scopeStyles : Collections.<String, AbstractNode>emptyMap();
  }

  private Map<NodeResolution, Edge> getEdgeTypes() {
    return this.edgeResolutionStyles != null ? this.edgeResolutionStyles : Collections.<NodeResolution, Edge>emptyMap();
  }

}
