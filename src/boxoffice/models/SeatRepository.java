package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Venue;
import boxoffice.database.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
