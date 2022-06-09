package query;

import hrdb.HRDB;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import rg.RESTGraph;
import rg.RESTGraphTraversal;

import static query.Utils.runQuery;

public class Top {
    public static void main(final String[] args) throws Exception {
        final var graph = new RESTGraph("http://localhost:3000/");

        graph.addRelationship(HRDB.EMPLOYEE_LABEL, HRDB.EMPLOYEE_DEPARTMENT_LABEL, HRDB.DEPARTMENT_LABEL);

        final var g = new RESTGraphTraversal(graph);

        runQuery(
                "Employees named Gene",
                g.verticesWithLabel(HRDB.EMPLOYEE_LABEL)
                        .has(HRDB.EMPLOYEE_FIRST_NAME, "Gene")
                        .elementMap()
        );

        runQuery(
                "Employees in Chicago",
                g.verticesWithLabel(HRDB.EMPLOYEE_LABEL)
                        .filter(
                                __.out(HRDB.EMPLOYEE_DEPARTMENT_LABEL)
                                        .has(HRDB.DEPARTMENT_LOCATION, "Chicago")
                        )
                        .elementMap()
        );
    }
}
