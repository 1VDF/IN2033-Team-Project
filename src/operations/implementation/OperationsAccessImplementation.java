package operations.implementation;

import boxoffice.database.TicketSale;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements data access methods for the Operations team to retrieve
 * ticket sales and revenue data from the database.
 * <p>
 * This class is used internally by {@link JDBC} to perform SQL queries
 * against the ticketing database.
 * </p>
 *
 * <p>All queries use prepared statements to prevent SQL injection.</p>
 *
 */
public class OperationsAccessImplementation {

    /**
     * Retrieves all ticket sales associated with a specific performance.
     *
     * @param connection      the active SQL connection.
     * @param performanceName the title of the performance.
     * @return a list of {@link TicketSale} objects.
     * @throws SQLException if a database error occurs.
     */
    public List<TicketSale> getTicketSalesBasedOnEvent(Connection connection, String performanceName) throws SQLException {
        List<TicketSale> ticketSales = new ArrayList<>();

        String query = "SELECT ts.* FROM ticket_sale ts " +
                "JOIN performance p ON ts.performance_id = p.performance_id " +
                "WHERE p.title = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, performanceName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TicketSale ticketSale = new TicketSale(
                        rs.getInt("ticket_sale_id"),
                        rs.getDouble("price"),
                        rs.getString("customer_id"),
                        rs.getInt("performance_id"),
                        rs.getString("seat_id"),
                        rs.getInt("discount_id"),
                        rs.getInt("group_id"),
                        rs.getInt("staff_id")
                );
                ticketSales.add(ticketSale);
            }
        }

        return ticketSales;
    }

    /**
     * Retrieves all ticket sales in the system.
     *
     * @param connection the active SQL connection.
     * @return a list of {@link TicketSale} objects.
     * @throws SQLException if a database error occurs.
     */
    public List<TicketSale> getAllTicketSales(Connection connection) throws SQLException {
        List<TicketSale> ticketSales = new ArrayList<>();
        String query = "SELECT * FROM ticket_sale";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TicketSale ticketSale = new TicketSale(
                        rs.getInt("ticket_sale_id"),
                        rs.getDouble("price"),
                        rs.getString("customer_id"),
                        rs.getInt("performance_id"),
                        rs.getString("seat_id"),
                        rs.getInt("discount_id"),
                        rs.getInt("group_id"),
                        rs.getInt("staff_id")
                );
                ticketSales.add(ticketSale);
            }
        }

        return ticketSales;
    }

    /**
     * Calculates total revenue from ticket sales for a specific performance.
     *
     * @param connection      the active SQL connection.
     * @param performanceName the title of the performance.
     * @return the total revenue, or -1 if an error occurs.
     */
    public int getRevenueBasedOnEvent(Connection connection, String performanceName) {
        int totalRevenue = 0;
        String query = "SELECT ts.price FROM ticket_sale ts " +
                "JOIN performance p ON ts.performance_id = p.performance_id " +
                "WHERE p.title = ? AND ts.price > 0";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, performanceName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalRevenue += rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return totalRevenue;
    }

    /**
     * Retrieves the total revenue from all ticket sales.
     *
     * @param connection the active SQL connection.
     * @return the total revenue, or -1 if an error occurs.
     */
    public int getTotalRevenue(Connection connection) {
        int totalRevenue = 0;
        String query = "SELECT price FROM ticket_sale WHERE price > 0";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalRevenue += rs.getDouble("price");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return totalRevenue;
    }

    /**
     * Generates a revenue report for a specific month and year.
     *
     * @param connection the active SQL connection.
     * @param year       the year to filter.
     * @param month      the month to filter (1–12).
     * @return a map containing:
     *         <ul>
     *           <li>{@code "revenue"} - total revenue (Double)</li>
     *           <li>{@code "tickets_sold"} - number of tickets sold (Integer)</li>
     *           <li>{@code "first_sale"} - timestamp of the first sale (Timestamp)</li>
     *           <li>{@code "last_sale"} - timestamp of the last sale (Timestamp)</li>
     *         </ul>
     * @throws SQLException if a database access error occurs.
     */
    public Map<String, Object> getMonthlyRevenueReport(Connection connection, int year, int month) throws SQLException {
        Map<String, Object> report = new HashMap<>();

        String query = "SELECT " +
                "SUM(price) AS monthly_revenue, " +
                "COUNT(*) AS tickets_sold, " +
                "MIN(sale_date) AS first_sale, " +
                "MAX(sale_date) AS last_sale " +
                "FROM ticket_sale " +
                "WHERE price > 0 " +
                "AND YEAR(sale_date) = ? " +
                "AND MONTH(sale_date) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                report.put("revenue", rs.getDouble("monthly_revenue"));
                report.put("tickets_sold", rs.getInt("tickets_sold"));
                report.put("first_sale", rs.getTimestamp("first_sale"));
                report.put("last_sale", rs.getTimestamp("last_sale"));
            }
        }

        return report;
    }
}
