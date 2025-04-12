package marketing.implementation;

import boxoffice.database.TicketSale;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * This class provides JDBC-based access to marketing-related data.
 * <p>
 * It serves as the primary entry point for the Marketing team to interact with the database.
 * This includes retrieving ticket sales data and revenue reports for marketing analysis.
 * </p>
 *
 * <p>
 * Upon instantiation, this class initializes the database connection and provides methods
 * to execute various predefined queries via the {@link MarketingAccessImpl} class.
 * </p>
 *
 */
public class JDBC {

    private final Connection connection;
    private MarketingAccessImpl marketingData;

    /**
     * Constructs a new JDBC instance and establishes a connection to the marketing database.
     *
     * @throws ClassNotFoundException if the JDBC driver class is not found
     * @throws SQLException if a database access error occurs
     */
    public JDBC() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://sst-stuproj.city.ac.uk/in2033t25";
        String user = "in2033t25_d";
        String pass = "N9CfacRwhIM";

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, user, pass);
        this.marketingData = new MarketingAccessImpl();
    }

    /**
     * Retrieves ticket sales for a given performance title.
     *
     * @param performanceName the name of the performance
     * @return a list of {@link TicketSale} records
     * @throws SQLException if a database access error occurs
     */
    public List<TicketSale> getTicketSalesBasedOnEvent(String performanceName) throws SQLException {
        return marketingData.getTicketSalesBasedOnEvent(connection, performanceName);
    }

    /**
     * Calculates the total revenue for a specific event.
     *
     * @param performanceName the name of the performance
     * @return the revenue generated for the event, or -1 if an error occurs
     */
    public int getRevenueBasedOnEvent(String performanceName) {
        return marketingData.getRevenueBasedOnEvent(connection, performanceName);
    }

    /**
     * Generates a monthly revenue report for a specified year and month.
     *
     * @param year the year (e.g. 2025)
     * @param month the month (1 = January, ..., 12 = December)
     * @return a map containing revenue metrics (e.g., revenue, tickets_sold, first_sale, last_sale)
     * @throws SQLException if a database access error occurs
     */
    public Map<String, Object> getMonthlyRevenueReport(int year, int month) throws SQLException {
        return marketingData.getMonthlyRevenueReport(connection, year, month);
    }

    /**
     * Closes the database connection.
     *
     * @throws SQLException if a database access error occurs during closing
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Entry point for testing database operations.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
    }
}
