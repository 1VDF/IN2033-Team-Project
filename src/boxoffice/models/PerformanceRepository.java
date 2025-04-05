package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Performance;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PerformanceRepository {

    public PerformanceRepository() {}

    private List<Performance> readPerformanceColumns(ResultSet rs) throws SQLException {
        List<Performance> performances = new ArrayList<>();
        while (rs.next()) {
            performances.add(new Performance(
                    rs.getInt("performance_id"),
                    rs.getString("title"),
                    rs.getString("performance_type"),
                    rs.getString("description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getInt("duration_minutes"),
                    rs.getInt("venue_id"),
                    rs.getString("venue_name")
            ));
        }
        rs.close();
        return performances;
    }

    public List<Performance> getAllPerformances() throws SQLException {
        String query = "SELECT p.*, v.name AS venue_name " +
                "FROM performance p " +
                "JOIN venue v ON p.venue_id = v.venue_id";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = statement.executeQuery(query)) {

            return readPerformanceColumns(rs);
        }
    }

    public boolean addPerformance(Performance performance) throws SQLException {
        String insertSQL = "INSERT INTO performance (title, performance_type, description, date, start_time, duration_minutes, venue_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setString(1, performance.getTitle());
            statement.setString(2, performance.getPerformanceType());
            statement.setString(3, performance.getDescription());
            statement.setDate(4, Date.valueOf(performance.getDate()));
            statement.setTime(5, Time.valueOf(performance.getStartTime()));
            statement.setInt(6, performance.getDurationMinutes());
            statement.setInt(7, performance.getVenueID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Method to get venue name by ID
    public String getVenueNameById(int venueId) throws SQLException {
        String venueName = "Unknown Venue";

        String query = "SELECT name FROM venue WHERE venue_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, venueId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    venueName = rs.getString("name");
                }
            }
        }
        return venueName;
    }

    public static Performance getPerformanceById(int performanceId) throws SQLException {
        String query = "SELECT p.*, v.name AS venue_name " +
                "FROM performance p " +
                "JOIN venue v ON p.venue_id = v.venue_id " +
                "WHERE p.performance_id = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, performanceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Performance(
                            rs.getInt("performance_id"),
                            rs.getString("title"),
                            rs.getString("performance_type"),
                            rs.getString("description"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getInt("duration_minutes"),
                            rs.getInt("venue_id"),
                            rs.getString("venue_name")
                    );
                }
            }
        }
        return null;
    }

    public static List<Performance> getPerformancesByDate(LocalDate date) throws SQLException {
        List<Performance> performances = new ArrayList<>();
        String query = "SELECT p.*, v.name as venue_name FROM performance p " +
                "JOIN venue v ON p.venue_id = v.venue_id " +
                "WHERE p.date = ? " +
                "ORDER BY p.start_time";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Performance performance = new Performance(
                        rs.getInt("performance_id"),
                        rs.getString("title"),
                        rs.getString("performance_type"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getInt("duration_minutes"),
                        rs.getInt("venue_id"),
                        rs.getString("venue_name")
                );
                performances.add(performance);
            }
        }
        return performances;
    }

}
