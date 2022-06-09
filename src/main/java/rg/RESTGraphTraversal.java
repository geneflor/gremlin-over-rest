package rg;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class RESTGraphTraversal extends GraphTraversalSource {
    public RESTGraphTraversal(final Graph graph) {
        super(graph);
    }

    public GraphTraversal<Vertex, Vertex> verticesWithLabel(final String label, final Object... vertexIds) {
        // a single null is [null]
        final Object[] ids = null == vertexIds ? new Object[] { null } : vertexIds;
        final GraphTraversalSource clone = this.clone();
        clone.getBytecode().addStep(GraphTraversal.Symbols.V, ids);
        final GraphTraversal.Admin<Vertex, Vertex> traversal = new DefaultGraphTraversal<>(clone);

        return traversal.addStep(new LabeledVertexStep<>(label, traversal, true, ids));
    }
}
