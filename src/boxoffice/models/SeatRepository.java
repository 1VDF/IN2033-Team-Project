package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Venue;
import boxoffice.database.Seat;

import java.sql.*;
import java.util.*;

public class SeatRepository {
    private final String Select_Statement = "Select * FROM `seat`";


    private static List<Seat> readSeatColumns(ResultSet rs) throws SQLException{
        List<Seat> seats = new ArrayList<>();
        while(rs.next()){
            seats.add(new Seat(rs.getString("seat_id"),
                    rs.getString("row_number"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_accesible")));
        }

        for (Seat s : seats) {
            System.out.println(s);
        }
        rs.close();
        return seats;
    }



    public static List<String> getAccessibleSeatIDs() throws SQLException {
        List<String> accessibleSeatIDs = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement()) {

            String query = "SELECT seat_id FROM `seat` WHERE is_accesible = 1";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                accessibleSeatIDs.add(rs.getString("seat_id"));
            }
            return accessibleSeatIDs;
        }
    }


    // Method to get the seating breakdown (available, accessible, sold, restricted view) for a specific performance
    public static Map<String, Integer> getSeatingBreakdown(int performanceId) throws SQLException {
        Map<String, Integer> seatingBreakdown = new HashMap<>();

        // SQL query to get the seat status breakdown for the specified performance
        String query = "SELECT seat_status, COUNT(*) as count " +
                "FROM seat " +
                "WHERE performance_id = ? " +
                "GROUP BY seat_status";

        try (Connection connection = DriverManager.getConnection(
                DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, performanceId);
            ResultSet rs = stmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                String status = rs.getString("seat_status");
                int count = rs.getInt("count");
                seatingBreakdown.put(status, count);
            }
        }

        return seatingBreakdown;
    }
}
