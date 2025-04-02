package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.database.Venue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceRepository {

    private static Venue venue;

    public PerformanceRepository(){
        this.venue = venue;
    }
    private static List<Performance> readPerformanceColumns(ResultSet rs) throws SQLException {
        List<Performance> performances = new ArrayList<>();
        while(rs.next()){
            performances.add(new Performance(rs.getInt("performance_id"),
                    rs.getString("title"),
                    rs.getString("performance_type"),
                    rs.getString("description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getInt("duration_minutes"),
                    (rs.getInt("venue_id"))));
        }
        for (Performance performance : performances) {
            System.out.println(performance);
        }
        rs.close();
        return performances;
    }

    public List<Performance> getAllPerformances() throws SQLException{
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String Select_Statement = "SELECT * FROM `performance`";
            ResultSet rs = statement.executeQuery(Select_Statement);
            return readPerformanceColumns(rs);
        }
    }

    public boolean addPerformance(Performance performance) throws SQLException {
        String insertSQL = "INSERT INTO performance (title, performance_type, description, date, start_time, duration_minutes, venue_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            // Set parameters for the prepared statement
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
}
