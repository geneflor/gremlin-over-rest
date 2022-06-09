package query;

import hrdb.HRDB;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static query.Utils.runQuery;

public class TopEmbedded {
    public static void main(final String[] args) throws Exception {
        final var graph = TinkerGraph.open();
        final var g = traversal().withEmbedded(graph);

        new HRDB(g).build();

        runQuery(
                "All employees",
                g.V()
                        .hasLabel(HRDB.EMPLOYEE_LABEL)
                        .has(HRDB.EMPLOYEE_FIRST_NAME, "Gene")
                        .elementMap()
        );

        runQuery(
                "All departments in Chicago",
                g.V()
                        .hasLabel(HRDB.DEPARTMENT_LABEL)
                        .has(HRDB.DEPARTMENT_LOCATION, "Chicago")
                        .elementMap()
        );

        runQuery(
                "Employees in Chicago",
                g.V()
                        .hasLabel(HRDB.EMPLOYEE_LABEL)
                        .filter(
                                __.out(HRDB.EMPLOYEE_DEPARTMENT_LABEL)
                                        .has(HRDB.DEPARTMENT_LOCATION, "Chicago")
                        )
                        .elementMap()
        );
    }
}
