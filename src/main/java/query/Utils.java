package query;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

public class Utils {
    static void runQuery(final String label, final GraphTraversal<?, ?> t) {
        System.out.println(label + ": [");

        while (t.hasNext()) {
            System.out.println("\t" + t.next());
        }

        System.out.println("]\n");
    }
}
