package hrdb;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class HRDB {
    public static final String DEPARTMENT_LABEL = "dept";
    public static final String DEPARTMENT_ID = "id";
    public static final String DEPARTMENT_NAME = "name";
    public static final String DEPARTMENT_LOCATION = "location";

    public static final String EMPLOYEE_LABEL = "emp";
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_FIRST_NAME = "firstName";
    public static final String EMPLOYEE_EMAIL = "email";

    public static final String EMPLOYEE_DEPARTMENT_LABEL = "department";

    private final GraphTraversalSource g;

    public HRDB(final GraphTraversalSource g) {
        this.g = g;
    }

    public Vertex addDepartment(final int id, final String name, final String location) {
        return g.addV(DEPARTMENT_LABEL)
                .property(DEPARTMENT_ID, id)
                .property(DEPARTMENT_NAME, name)
                .property(DEPARTMENT_LOCATION, location)
                .next();
    }

    public Vertex addEmployee(final int id, final String firstName, final String email, final Vertex department) {
        final var employeeVertex =
                g.addV(EMPLOYEE_LABEL)
                .property(EMPLOYEE_ID, id)
                .property(EMPLOYEE_FIRST_NAME, firstName)
                .property(EMPLOYEE_EMAIL, email)
                .next();

        g.addE(EMPLOYEE_DEPARTMENT_LABEL)
                .from(employeeVertex)
                .to(department)
                .iterate();

        return employeeVertex;
    }

    public void build() {
        final var sales = addDepartment(1, "Sales", "New York");
        final var marketing = addDepartment(2, "Marketing", "Chicago");
        final var eng = addDepartment(3, "Engineering", "Austin");

        addEmployee(1, "Gene", "gene@oracle.com", eng);
        addEmployee(2, "Jane", "jane@oracle.com", sales);
        addEmployee(3, "Shashi", "shashi@oracle.com", eng);
        addEmployee(4, "Alex", "alex@oracle.com", marketing);
        addEmployee(5, "Jaewoong", "jaewoong@oracle.com", eng);
        addEmployee(6, "Larry", "larry@oracle.com", sales);
        addEmployee(7, "Chris", "chris@oracle.com", marketing);
    }
}
