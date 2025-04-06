package marketing.implementation;

import boxoffice.database.TicketSale;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the MarketingAccess interface.
 * <p>
 * Through the implementation, access to the booking and customer data is granted.
 * This class connects to the database and executes SQL qureries to retrieve the relevant information,
 * matching with the requirements produced by the Marketing Team.
 * </p>
 *
 * @author Denis Volocaru
 * @version 2.0
 */
public class MarketingAccessImpl {
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
                        rs.getInt("group_booking_id"),
                        rs.getInt("staff_id")
                );
                ticketSales.add(ticketSale);
            }
        }
        return ticketSales;
    }

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
            return -1; // Indicate error
        }

        return totalRevenue;
    }
}