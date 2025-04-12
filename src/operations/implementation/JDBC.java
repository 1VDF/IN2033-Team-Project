package operations.implementation;

import boxoffice.database.TicketSale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The {@code JDBC} class serves as the main interface for retrieving
 * ticket sales and revenue data from the database using JDBC.
 * <p>
 * It establishes a connection to the database and delegates data retrieval
 * to {@link OperationsAccessImplementation}.
 * </p>
 *
 * <p>This class is intended for use by the Operations team.</p>
 *
 */
public class JDBC {

    private final Connection connection;
    private OperationsAccessImplementation operationsData;

    /**
     * Constructs a new {@code JDBC} instance and establishes a connection
     * to the MySQL database using predefined credentials.
     *
     * @throws ClassNotFoundException if the JDBC driver class cannot be found.
     * @throws SQLException if a database access error occurs.
     */
    public JDBC() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://sst-stuproj.city.ac.uk/in2033t25";
        String user = "in2033t25_d";
        String pass = "N9CfacRwhIM";

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, user, pass);
        this.operationsData = new OperationsAccessImplementation();
    }

    /**
     * Retrieves all ticket sales associated with a given performance title.
     *
     * @param performanceName the name of the performance.
     * @return a list of {@link TicketSale} objects.
     * @throws SQLException if a database error occurs.
     */
    public List<TicketSale> getTicketSalesBasedOnEvent(String performanceName) throws SQLException {
        return operationsData.getTicketSalesBasedOnEvent(connection, performanceName);
    }

    /**
     * Retrieves total revenue generated from a specific performance.
     *
     * @param performanceName the title of the performance.
     * @return the total revenue as an integer, or -1 if an error occurs.
     */
    public int getRevenueBasedOnEvent(String performanceName) {
        return operationsData.getRevenueBasedOnEvent(connection, performanceName);
    }

    /**
     * Generates a monthly revenue report for a specific year and month.
     *
     * @param year  the year of the report.
     * @param month the month of the report (1-12).
     * @return a map containing revenue details, ticket counts, and date range.
     * @throws SQLException if a database access error occurs.
     */
    public Map<String, Object> getMonthlyRevenueReport(int year, int month) throws SQLException {
        return operationsData.getMonthlyRevenueReport(connection, year, month);
    }

    /**
     * Retrieves all ticket sales records in the database.
     *
     * @return a list of all {@link TicketSale} entries.
     * @throws SQLException if a database error occurs.
     */
    public List<TicketSale> getAllTicketSales() throws SQLException {
        return operationsData.getAllTicketSales(connection);
    }

    /**
     * Retrieves the total revenue generated from all ticket sales.
     *
     * @return the total revenue, or -1 if an error occurs.
     * @throws SQLException if a database access error occurs.
     */
    public int getTotalRevenue() throws SQLException {
        return operationsData.getTotalRevenue(connection);
    }

    /**
     * Closes the database connection.
     *
     * @throws SQLException if an error occurs while closing the connection.
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Entry point for testing or standalone usage.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
    }
}
