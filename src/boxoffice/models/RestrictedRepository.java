package boxoffice.models;

import boxoffice.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestrictedRepository {
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

    public static void clearPerformanceRestrictions(int performanceId) throws SQLException {
        String sql = "DELETE FROM restricted_view_seat WHERE performance_id = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, performanceId);
            stmt.executeUpdate();
        }
    }

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

    public static List<String> getPartialRestrictedSeats() throws SQLException{
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
