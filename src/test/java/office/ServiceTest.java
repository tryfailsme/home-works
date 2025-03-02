package office;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ServiceTest {

    private static final String DB_URL = "jdbc:h2:.\\OfficeTest";
    private static final Service sv = new Service(DB_URL);

    @BeforeEach
    public void setUp() {
        sv.createDB();
    }

    @Test
    public void testCreateDB() throws SQLException {
        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            st.executeUpdate("UPDATE Department SET NAME = NULL");
            st.executeUpdate("UPDATE Employee SET NAME = NULL, DepartmentID = NULL");
            sv.createDB();

            List<String> departments = Arrays.asList("Accounting", "IT", "HR");
            ResultSet rs = st.executeQuery("SELECT NAME FROM Department ORDER BY ID");
            for (String dep : departments) {
                assertTrue(rs.next());
                assertEquals(dep, rs.getString("NAME"));
            }
            assertFalse(rs.next());

            Object[][] employees = {{"Pete", 1}, {"Ann", 1}, {"Liz", 2}, {"Tom", 2}, {"Todd", 3}};
            rs = st.executeQuery("SELECT NAME, DepartmentID FROM Employee ORDER BY ID");
            for (Object[] empl : employees) {
                assertTrue(rs.next());
                assertEquals(empl[0], rs.getString("NAME"));
                assertEquals(empl[1], rs.getInt("DepartmentID"));
            }
            assertFalse(rs.next());
        }
    }

    @Test
    public void testAddDepartment() throws SQLException {
        Department department = new Department(3, "New");
        sv.addDepartment(department);
        department.setDepartmentID(4);
        sv.addDepartment(department);

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM Department WHERE ID = 3");
            assertTrue(rs.next());
            assertNotEquals("New", rs.getString(2));

            rs = st.executeQuery("SELECT * FROM Department WHERE ID = 4");
            assertTrue(rs.next());
            assertEquals("New", rs.getString(2));
        }
    }

    @Test
    public void testRemoveDepartment() throws SQLException {
        Department department = new Department(2, "IT");
        sv.removeDepartment(department);

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Department WHERE ID = 2");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));

            rs = st.executeQuery("SELECT COUNT(*) FROM Employee WHERE DepartmentID = 2");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
        }
    }

    @Test
    public void testAddEmployee() throws SQLException {
        Employee employee = new Employee(1, "New", 3);
        sv.addEmployee(employee);
        employee = new Employee(6, "New", 4);
        sv.addEmployee(employee);
        employee = new Employee(7, "New", 3);
        sv.addEmployee(employee);

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM Employee WHERE ID = 5");
            assertTrue(rs.next());
            assertNotEquals("New", rs.getString(2));

            rs = st.executeQuery("SELECT COUNT(*) FROM Employee WHERE ID = 6");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));

            rs = st.executeQuery("SELECT * FROM Employee WHERE ID = 7");
            assertTrue(rs.next());
            assertEquals("New", rs.getString(2));
            assertEquals(3, rs.getInt(3));
        }
    }

    @Test
    public void testRemoveEmployee() throws SQLException {
        Employee employee = new Employee(1, "New", 3);
        sv.removeEmployee(employee);

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Employee WHERE ID = 1");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
        }
    }
}