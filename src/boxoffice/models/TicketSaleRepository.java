package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Performance;
import boxoffice.database.TicketSale;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketSaleRepository {

    // Add new ticket sale record to the database
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

            // Handle nullable fields
            Integer discountId = ticket.getDiscountID();
            Integer groupBookingId = ticket.getGroupBookingID();

            if (discountId == null) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, discountId);
            }

            if (groupBookingId == null) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, groupBookingId);
            }

            stmt.setInt(8, ticket.getStaffID());
            return stmt.execute();
        }
    }

    // Fetch a TicketSale by its ID (returns a TicketSale object)
    public static TicketSale getTicketSaleById(String ticketSaleId) throws SQLException {
        String query = "SELECT * FROM ticket_sale WHERE ticket_sale_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, ticketSaleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TicketSale(
                        rs.getInt("ticket_sale_id"),
                        rs.getDouble("price"),
                        rs.getString("customer_id"),
                        rs.getInt("performance_id"),
                        rs.getString("seat_id"),
                        rs.getInt("discount_id"),
                        rs.getInt("group_id"),
                        rs.getInt("staff_id")
                );
            }
            return null;  // No TicketSale found
        }
    }

    // Mark ticket as refunded
    public static boolean markAsRefunded(int ticketSaleId) throws SQLException {
        String sql = "UPDATE ticket_sale SET refunded = TRUE WHERE ticket_sale_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketSaleId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Get all ticket sales from the database
    public static List<TicketSale> getAllTicketSales() throws SQLException {
        List<TicketSale> ticketSales = new ArrayList<>();
        String query = "SELECT * FROM ticket_sale";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

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

    // Get all booked seats for a specific performance
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

    // Get refundable tickets (those without a refund record or with a processing refund)
    public static List<TicketSale> getRefundableTickets() throws SQLException {
        List<TicketSale> refundableTickets = new ArrayList<>();
        String query =
                "SELECT ts.* FROM ticket_sale ts " +
                        "LEFT JOIN refund r ON ts.ticket_sale_id = r.ticket_sale_id " +
                        "WHERE r.ticket_sale_id IS NULL " +
                        "OR r.refund_status = 'Processing'";  // Including those with 'Processing' status

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TicketSale ticketSale = new TicketSale(
                        rs.getInt("ticket_sale_id"),
                        rs.getDouble("price"),
                        rs.getString("customer_id"),
                        rs.getInt("performance_id"),
                        rs.getString("seat_id"),
                        rs.getInt("discount_id"),
                        rs.getInt("group_id"), // Fix to group_id
                        rs.getInt("staff_id")
                );
                refundableTickets.add(ticketSale);
            }
        }

        return refundableTickets;
    }

    public static List<TicketSale> getTicketsForRefund(int performanceId, String customerName) throws SQLException {
        List<TicketSale> tickets = new ArrayList<>();
        String query = "SELECT ts.* FROM ticket_sale ts " +
                "JOIN customer c ON ts.customer_id = c.customer_id " +
                "WHERE ts.performance_id = ? " +
                "AND c.customer_name LIKE ? " +
                "AND ts.refunded = 0";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, performanceId);
            stmt.setString(2, "%" + customerName + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TicketSale ticket = new TicketSale(
                        rs.getInt("ticket_sale_id"),
                        rs.getDouble("price"),
                        rs.getString("customer_id"),
                        rs.getInt("performance_id"),
                        rs.getString("seat_id"),
                        rs.getInt("discount_id"),
                        rs.getInt("group_id"),
                        rs.getInt("staff_id")
                );
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public static Timestamp getSaleTimestamp(int ticketSaleId) throws SQLException {
        String query = "SELECT sale_date FROM ticket_sale WHERE ticket_sale_id = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketSaleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getTimestamp("sale_date") : null;
        }
    }



}