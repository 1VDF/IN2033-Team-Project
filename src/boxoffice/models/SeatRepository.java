package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Location;
import boxoffice.database.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatRepository {
    private final String Select_Statement = "Select * FROM `Seat`";


    private static List<Seat> readSeatColumns(ResultSet rs) throws SQLException{
        List<Seat> seats = new ArrayList<>();
        while(rs.next()){
            seats.add(new Seat(rs.getString("row_number"),
                    rs.getInt("seat_number"),
                    rs.getString("restricted_view"),
                    rs.getBoolean("is_accesible"),
                    rs.getBoolean("is_booked"),
                    rs.getInt("room_id")));
        }
        for (Seat s : seats) {
            System.out.println(s);
        }
        rs.close();
        return seats;
    }

    public List<Seat> getAllSeatsByHall(Location location) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String whereClause = "WHERE room_id = " + location.getRoomId();
            ResultSet rs = statement.executeQuery(Select_Statement + whereClause);
            return readSeatColumns(rs);
        }
    }

    public void setBookedSeat(Seat seat) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement()) {
            String sql = "UPDATE `Seat` SET `is_booked` = '1' WHERE `Seat`.`row_number` = " + seat.getRowNumber() +
                    " AND `Seat`.`seat_number` = " + seat.getSeatNumber() +
                    " AND `Seat`.`room_id` = " + seat.getLocation().getRoomId();
            int rowsAffected = statement.executeUpdate(sql);

            if(rowsAffected == 0){
                throw new SQLException("Failed to book seat: Seat not found or already booked");
            }
        }
    }

    public void setRestrictedView(Seat seat, String restrictedType) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement()) {
            String sql = "UPDATE `Seat` SET `restricted_view` = " + restrictedType +
                    " WHERE `Seat`.`row_number` = " + seat.getRowNumber() +
                    " AND `Seat`.`seat_number` = " + seat.getSeatNumber() +
                    " AND `Seat`.`room_id` = " + seat.getLocation().getRoomId();
            int rowsAffected = statement.executeUpdate(sql);

            if(rowsAffected == 0){
                throw new SQLException("Failed to set restricted view: Seat not found, already marked as restricted or" +
                        "restriction out of bounds");
            }
        }
    }


    public List<Seat> getAllAccesibleSeatsByHall(Location location) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String whereClause = "WHERE is_accesible = 1 AND room_id = " + location.getRoomId();
            ResultSet rs = statement.executeQuery(Select_Statement + whereClause);
            return readSeatColumns(rs);
        }
    }

}
