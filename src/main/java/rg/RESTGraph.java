package rg;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.Validate;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class RESTGraph implements Graph {
    private final String endpoint;
    private final EmptyGraphFeatures features = new EmptyGraphFeatures();
    private final Map<String, String> relationships = new HashMap<>();

    public RESTGraph(final String endpoint) {
        Validate.isTrue(endpoint.endsWith("/"));
        this.endpoint = endpoint;
    }

    public void addRelationship(final String fromLabel, final String edgeLabel, final String toLabel) {
        relationships.put(fromLabel + "." + edgeLabel, toLabel);
    }

    public Iterator<Vertex> verticesWithLabel(final String label, final Object... vertexIds) {
        final var vertices = new ArrayList<Vertex>();
        final Object[] objects;

        objects = httpGet(label, Object[].class);

        for (final var o : objects) {
            final var map = (Map<String, Object>) o;

            vertices.add(new RESTVertex(this, label, map));
        }

        return vertices.iterator();
    }

    private <T> T httpGet(final String path, Class<T> cls) {
        try {
            return new ObjectMapper().readValue(new URL(endpoint + path), cls);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Features features() {
        return features;
    }

    @Override
    public Vertex addVertex(final Object... keyValues) {
        throw Exceptions.vertexAdditionsNotSupported();
    }

    @Override
    public <C extends GraphComputer> C compute(final Class<C> graphComputerClass) {
        throw Exceptions.graphComputerNotSupported();
    }

    @Override
    public GraphComputer compute() {
        throw Exceptions.graphComputerNotSupported();
    }

    @Override
    public Transaction tx() {
        throw Exceptions.transactionsNotSupported();
    }

    @Override
    public Variables variables() {
        throw Exceptions.variablesNotSupported();
    }

    @Override
    public Configuration configuration() {
        throw new IllegalStateException();
    }

    @Override
    public void close() {
    }

    @Override
    public Iterator<Vertex> vertices(final Object... vertexIds) {
        throw new UnsupportedOperationException("Cannot iterate untyped vertices");
    }

    @Override
    public Iterator<Edge> edges(final Object... edgeIds) {
        throw new UnsupportedOperationException("Cannot iterate untyped edges");
    }

    @Override
    public String toString() {
        return StringFactory.graphString(this, "empty");
    }

    public Vertex getOutVertex(final String fromLabel, final String fieldKey, final Object id) {
        final var key = fromLabel + "." + fieldKey;
        final var targetLabel = relationships.get(key);

        if (targetLabel == null) {
            throw new UnsupportedOperationException(key);
        }

        final Map<String, Object> map = httpGet(targetLabel + "/" + id, Map.class);

        return new RESTVertex(this, targetLabel, map);
    }

    /**
     * Features defined such that they support immutability but allow all other possibilities.
     */
    public static final class EmptyGraphFeatures implements Features {
        private GraphFeatures graphFeatures = new RESTGraphGraphFeatures();
        private VertexFeatures vertexFeatures = new RESTGraphVertexFeatures();
        private EdgeFeatures edgeFeatures = new RESTGraphEdgeFeatures();
        private EdgePropertyFeatures edgePropertyFeatures = new RESTGraphEdgePropertyFeatures();
        private VertexPropertyFeatures vertexPropertyFeatures = new RESTGraphVertexPropertyFeatures();

        private EmptyGraphFeatures() {
        }

        @Override
        public GraphFeatures graph() {
            return graphFeatures;
        }

        @Override
        public VertexFeatures vertex() {
            return vertexFeatures;
        }

        @Override
        public EdgeFeatures edge() {
            return edgeFeatures;
        }

        /**
         * Graph features defined such that they support immutability but allow all other possibilities.
         */
        public final class RESTGraphGraphFeatures implements GraphFeatures {
            @Override
            public boolean supportsPersistence() {
                return false;
            }

            @Override
            public boolean supportsTransactions() {
                return false;
            }

            @Override
            public boolean supportsThreadedTransactions() {
                return false;
            }

            @Override
            public VariableFeatures variables() {
                return null;
            }

            @Override
            public boolean supportsComputer() {
                return false;
            }
        }

        /**
         * Vertex features defined such that they support immutability but allow all other possibilities.
         */
        public final class RESTGraphVertexFeatures extends RESTGraphElementFeatures implements VertexFeatures {
            @Override
            public VertexProperty.Cardinality getCardinality(final String key) {
                // probably not much hurt here in returning list...it's an "empty graph"
                return VertexProperty.Cardinality.list;
            }

            @Override
            public boolean supportsAddVertices() {
                return false;
            }

            @Override
            public boolean supportsRemoveVertices() {
                return false;
            }

            @Override
            public VertexPropertyFeatures properties() {
                return vertexPropertyFeatures;
            }
        }

        /**
         * Edge features defined such that they support immutability but allow all other possibilities.
         */
        public final class RESTGraphEdgeFeatures extends RESTGraphElementFeatures implements EdgeFeatures {
            @Override
            public boolean supportsAddEdges() {
                return false;
            }

            @Override
            public boolean supportsRemoveEdges() {
                return false;
            }

            @Override
            public EdgePropertyFeatures properties() {
                return edgePropertyFeatures;
            }
        }

        /**
         * Vertex Property features defined such that they support immutability but allow all other possibilities.
         */
        public final class RESTGraphVertexPropertyFeatures implements VertexPropertyFeatures {
            @Override
            public boolean supportsRemoveProperty() {
                return false;
            }
        }

        /**
         * Edge property features defined such that they support immutability but allow all other possibilities.
         */
        public final class RESTGraphEdgePropertyFeatures implements EdgePropertyFeatures {}

        /**
         * Vertex features defined such that they support immutability but allow all other possibilities.
         */
        public abstract class RESTGraphElementFeatures implements ElementFeatures {
            @Override
            public boolean supportsAddProperty() {
                return false;
            }

            @Override
            public boolean supportsRemoveProperty() {
                return false;
            }
        }
    }
}
