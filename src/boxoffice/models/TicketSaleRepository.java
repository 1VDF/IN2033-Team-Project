package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.TicketSale;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static java.sql.Types.NULL;

public class TicketSaleRepository {
    public static boolean addTicketSale(TicketSale ticket) throws SQLException {
        String sql = "INSERT INTO `ticket_sale` (ticket_sale_id, price, customer_id, performance_id, seat_id, discount_id, group_id, staff_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticket.getTicketSaleId());
            stmt.setDouble(2, ticket.getPrice());
            stmt.setString(3, ticket.getCustomerID());
            stmt.setInt(4, ticket.getPerformanceID());
            stmt.setString(5, ticket.getSeatID());
            if (ticket.getDiscountID() == NULL) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, ticket.getDiscountID());
            }
            if (ticket.getDiscountID() == NULL) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, ticket.getGroupBookingID());
            }
            stmt.setInt(8, ticket.getStaffID());
            return stmt.execute();
        }
    }


    // Get all booked seats for a performance
    public static Set<String> getBookedSeats(int performanceId) throws SQLException {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT seat_id FROM ticket_sale WHERE performance_id = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, performanceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_id"));
            }
        }
        return bookedSeats;
    }
}