package rg;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class RESTVertex implements Vertex {
    private final RESTGraph graph;
    private final UUID id;
    private final String label;
    private Map<String, Object> properties = new HashMap<>();

    public RESTVertex(final RESTGraph graph, final String label, final Map<String, Object> properties) {
        this.graph = graph;
        this.id = UUID.randomUUID();
        this.label = label;
        this.properties = properties;
    }

    @Override
    public Iterator<Edge> edges(final Direction direction, final String... edgeLabels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Vertex> vertices(final Direction direction, final String... edgeLabels) {
        final var subset = new ArrayList<Vertex>();

        for (final String key : edgeLabels) {
            subset.add(graph.getOutVertex(label, key, properties.get(key)));
        }

        return subset.iterator();
    }

    @Override
    public <V> Iterator<VertexProperty<V>> properties(final String... propertyKeys) {
        final String[] keysToIterate =
                propertyKeys.length == 0 ?
                        properties.keySet().toArray(String[]::new) :
                        propertyKeys;

        final var subset = new ArrayList<VertexProperty<V>>();

        for (final String key : keysToIterate) {
            subset.add(new RESTVertexProperty<V>(this, key, (V) properties.get(key)));
        }

        return subset.iterator();
    }

    @Override
    public Object id() {
        return id;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public Graph graph() {
        return graph;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Edge addEdge(final String label, final Vertex inVertex, final Object... keyValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> VertexProperty<V> property(final VertexProperty.Cardinality cardinality, final String key, final V value, final Object... keyValues) {
        throw new UnsupportedOperationException();
    }
}
