package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.TicketSale;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.sql.Types.NULL;

/**
 * The TicketSaleRepository class provides methods for interacting with ticket sales data in the database.
 * It includes operations for adding, updating, and fetching ticket sales records, managing refunds and check-ins,
 * and retrieving booking information such as booked seats.
 */
public class TicketSaleRepository {

    /**
     * Adds a new ticket sale record to the database.
     *
     * This method inserts a new record into the `ticket_sale` table using the details of the given
     * TicketSale object. The method handles nullable fields, such as discount ID and group booking ID.
     *
     * @param ticket The TicketSale object containing the details of the ticket sale to be added.
     * @return A boolean indicating whether the insert was successful (true if successful).
     * @throws SQLException If a database access error occurs.
     */
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

            if (discountId == NULL) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, discountId);
            }

            if (groupBookingId == NULL) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, groupBookingId);
            }

            stmt.setInt(8, ticket.getStaffID());
            return stmt.execute();
        }
    }

    /**
     * Retrieves a TicketSale object by its ID.
     *
     * This method queries the `ticket_sale` table for a ticket sale with the specified ID and returns
     * the corresponding TicketSale object. If no matching ticket sale is found, it returns null.
     *
     * @param ticketSaleId The ID of the ticket sale to retrieve.
     * @return The TicketSale object corresponding to the given ticketSaleId, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
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
            return null;
        }
    }

    /**
     * Marks a ticket as refunded.
     *
     * This method updates the `ticket_sale` table to mark the ticket with the given ID as refunded.
     * It sets the `refunded` column to true for the specified ticketSaleId.
     *
     * @param ticketSaleId The ID of the ticket sale to be marked as refunded.
     * @return A boolean indicating whether the update was successful (true if successful).
     * @throws SQLException If a database access error occurs.
     */
    public static boolean markAsRefunded(int ticketSaleId) throws SQLException {
        String sql = "UPDATE ticket_sale SET refunded = TRUE WHERE ticket_sale_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketSaleId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves the set of booked seats for a specific performance.
     *
     * This method queries the `ticket_sale` table to retrieve the seat IDs that have been booked for
     * the specified performance. It returns a set of seat IDs.
     *
     * @param performanceId The ID of the performance for which to retrieve booked seats.
     * @return A set of seat IDs that have been booked for the specified performance.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Retrieves a list of tickets for refund based on performance ID and customer name.
     *
     * This method retrieves all tickets for a specific performance and customer name that have not yet been refunded.
     * The tickets are returned as a list of TicketSale objects.
     *
     * @param performanceId The ID of the performance to filter tickets by.
     * @param customerName The name of the customer to filter tickets by.
     * @return A list of TicketSale objects that match the specified criteria.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Retrieves the timestamp of when a ticket sale occurred.
     *
     * This method retrieves the sale date of the ticket sale record identified by the provided ticketSaleId.
     * It returns a Timestamp object representing the date and time the sale occurred.
     *
     * @param ticketSaleId The ID of the ticket sale to retrieve the timestamp for.
     * @return A Timestamp representing the sale date, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
    public static Timestamp getSaleTimestamp(int ticketSaleId) throws SQLException {
        String query = "SELECT sale_date FROM ticket_sale WHERE ticket_sale_id = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketSaleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getTimestamp("sale_date") : null;
        }
    }

    /**
     * Marks a ticket as checked in.
     *
     * This method updates the `ticket_sale` table to mark the ticket with the given ID as checked in.
     * It sets the `checked_in` column to true for the specified ticketSaleId.
     *
     * @param ticketSaleId The ID of the ticket sale to be marked as checked in.
     * @return A boolean indicating whether the update was successful (true if successful).
     * @throws SQLException If a database access error occurs.
     */
    public static boolean markAsCheckedIn(int ticketSaleId) throws SQLException {
        String sql = "UPDATE ticket_sale SET checked_in = TRUE WHERE ticket_sale_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketSaleId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves a list of tickets sold to a specific customer that have not been checked in.
     *
     * This method retrieves all tickets for a specific customer (identified by their name) that
     * have not yet been checked in. It returns a list of TicketSale objects.
     *
     * @param customerName The name of the customer to filter tickets by.
     * @return A list of TicketSale objects for the specified customer that have not been checked in.
     * @throws SQLException If a database access error occurs.
     */
    public static List<TicketSale> getTicketsByCustomerName(String customerName) throws SQLException {
        List<TicketSale> tickets = new ArrayList<>();
        String query = "SELECT ts.* FROM ticket_sale ts " +
                "JOIN customer c ON ts.customer_id = c.customer_id " +
                "WHERE c.customer_name LIKE ? " +
                "AND ts.checked_in = 0";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + customerName + "%");
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
                ticket.setCheckedIn(rs.getBoolean("checked_in"));
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
