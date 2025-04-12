package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The SeatRepository class provides methods for interacting with the seat data in the database,
 * including retrieving all seats from the main hall, fetching accessible seats, and processing
 * seat data from a ResultSet.
 */
public class SeatRepository {

    /**
     * Processes a ResultSet and maps it to a list of Seat objects.
     *
     * This method iterates through the given ResultSet and creates a list of `Seat` objects
     * from the data in the `seat_id`, `row_number`, `seat_number`, and `is_accesible` columns.
     *
     * @param rs The ResultSet containing seat data fetched from the database.
     * @return A list of `Seat` objects corresponding to the rows in the ResultSet.
     * @throws SQLException If a database access error occurs or if a ResultSet is closed unexpectedly.
     */
    private static List<Seat> readSeatColumns(ResultSet rs) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        while (rs.next()) {
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

    /**
     * Retrieves all seat IDs that are accessible.
     *
     * This method queries the database for all seats where the `is_accesible` flag is set to true.
     * It returns a list of seat IDs that correspond to accessible seats.
     *
     * @return A list of seat IDs that are accessible.
     * @throws SQLException If a database access error occurs.
     */
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

    /**
     * Retrieves all seats from the main hall.
     *
     * This method queries the database to retrieve all seats from the main hall. It returns a list
     * of `Seat` objects corresponding to all seats in the `seat` table.
     *
     * @return A list of `Seat` objects representing all seats in the main hall.
     * @throws SQLException If a database access error occurs.
     */
    public static List<Seat> getAllSeatsFromMainHall() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String Select_Statement = "Select * FROM `seat`";
            ResultSet rs = statement.executeQuery(Select_Statement);
            return readSeatColumns(rs);
        }
    }
}
