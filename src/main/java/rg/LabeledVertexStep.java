package rg;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.structure.Vertex;

class LabeledVertexStep<S> extends GraphStep<S, Vertex> {
    public LabeledVertexStep(final String label, final Traversal.Admin traversal, final boolean isStart, final Object... ids) {
        super(traversal, Vertex.class, isStart, ids);

        this.setIteratorSupplier(
                () -> ((RESTGraph) this.getTraversal().getGraph().get()).verticesWithLabel(label, this.ids)
        );
    }
}
