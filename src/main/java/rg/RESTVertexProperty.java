package rg;

import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RESTVertexProperty<V> implements VertexProperty<V> {
    private final RESTVertex vertex;
    private final String key;
    private final V value;

    public RESTVertexProperty(final RESTVertex vertex, final String key, final V value) {
        this.vertex = vertex;
        this.key = key;
        this.value = value;
    }

    @Override
    public Vertex element() {
        return vertex;
    }

    @Override
    public <U> Iterator<Property<U>> properties(final String... propertyKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object id() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Property<V> property(final String key, final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public V value() throws NoSuchElementException {
        return value;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
