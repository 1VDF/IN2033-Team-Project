package boxoffice.models;

import boxoffice.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RestrictedRepository class provides methods for interacting with the restricted view seats
 * in the database, including fetching restricted seat information, clearing seat restrictions,
 * and setting or retrieving seat restrictions for a specific performance.
 */
public class RestrictedRepository {

    /**
     * Retrieves all restricted seats for a given performance.
     *
     * This method queries the database for all seats marked as restricted for a specified
     * performance. It returns a map where the key is the seat ID, and the value is the type
     * of restriction applied (e.g., "partial", "full").
     *
     * @param performanceId The ID of the performance for which the restricted seats are being fetched.
     * @return A map of seat IDs to restriction types.
     * @throws SQLException If a database access error occurs.
     */
    public static Map<String, String> getRestrictedSeats(int performanceId) throws SQLException {
        Map<String, String> restrictedSeats = new HashMap<>();
        String sql = "SELECT seat_id, type FROM restricted_view_seat WHERE performance_id = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, performanceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                restrictedSeats.put(rs.getString("seat_id"), rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return restrictedSeats;
    }

    /**
     * Clears all restrictions for a given performance.
     *
     * This method deletes all records from the `restricted_view_seat` table for a specified
     * performance, effectively clearing any restrictions placed on seats for that performance.
     *
     * @param performanceId The ID of the performance for which to clear the restrictions.
     * @throws SQLException If a database access error occurs.
     */
    public static void clearPerformanceRestrictions(int performanceId) throws SQLException {
        String sql = "DELETE FROM restricted_view_seat WHERE performance_id = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, performanceId);
            stmt.executeUpdate();
        }
    }

    /**
     * Sets a restriction on a specific seat for a performance.
     *
     * This method inserts a new record into the `restricted_view_seat` table to apply a
     * restriction on a given seat for a specific performance.
     *
     * @param performanceId The ID of the performance to which the seat restriction applies.
     * @param seatId The ID of the seat to be restricted.
     * @param restrictionType The type of restriction to apply to the seat (e.g., "partial", "full").
     * @throws SQLException If a database access error occurs.
     */
    public static void setSeatRestriction(int performanceId, String seatId, String restrictionType) throws SQLException {
        String sql = "INSERT INTO restricted_view_seat (performance_id, seat_id, type) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, performanceId);
            stmt.setString(2, seatId);
            stmt.setString(3, restrictionType);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all seats that have a partial restriction type.
     *
     * This method queries the database to retrieve all seats that have a "partial" restriction.
     * It returns a list of seat IDs that have this restriction.
     *
     * @return A list of seat IDs with partial restrictions.
     * @throws SQLException If a database access error occurs.
     */
    public static List<String> getPartialRestrictedSeats() throws SQLException {
        List<String> partialrestrictedSeats = new ArrayList<>();
        String sql = "SELECT seat_id FROM restricted_view_seat WHERE type = 'partial'";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                partialrestrictedSeats.add(rs.getString("seat_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return partialrestrictedSeats;
    }
}
