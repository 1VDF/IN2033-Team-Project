package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The StaffRepository class provides methods for managing staff data in the database,
 * including authentication, adding new staff, deleting staff, and fetching staff information.
 */
public class StaffRepository {

    /**
     * Authenticates a staff member based on their email and password.
     *
     * This method checks the provided email and password against the database records.
     * If a match is found, it returns a `Staff` object representing the authenticated staff member.
     *
     * @param email The email address of the staff member.
     * @param password The password of the staff member.
     * @return The authenticated `Staff` object, or null if authentication fails.
     * @throws SQLException If a database access error occurs.
     */
    public Staff authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM staff WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleString = rs.getString("role").replace(" ", "");
                Staff.Role role = Staff.Role.valueOf(roleString);
                return new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        role,
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    /**
     * Adds a new staff member to the database.
     *
     * This method inserts a new record into the `staff` table with the details of the given `Staff` object.
     *
     * @param staff The `Staff` object containing the details of the staff member to be added.
     * @throws SQLException If a database access error occurs.
     */
    public void addStaff(Staff staff) throws SQLException {
        String query = "INSERT INTO staff (first_name, last_name, role, email, password) " +
                "VALUES (?, ?, ?, ?, ?)";
        ;
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, staff.getFirstName());
            stmt.setString(2, staff.getLastName());

            String roleString = switch (staff.getRole()) {
                case Staff -> "Staff";
                case DeputyManager -> "Deputy Manager";
                case Manager -> "Manager";
            };
            stmt.setString(3, roleString);
            stmt.setString(4, staff.getEmail());
            stmt.setString(5, staff.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add staff: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a staff member from the database based on their ID.
     *
     * This method removes the staff record from the `staff` table using the provided staff ID.
     *
     * @param staffId The ID of the staff member to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteStaff(int staffId) throws SQLException {
        String query = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a list of all staff members from the database.
     *
     * This method queries the `staff` table and returns a list of `Staff` objects representing all
     * staff members in the system.
     *
     * @return A list of all staff members.
     * @throws SQLException If a database access error occurs.
     */
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Staff.Role role = Staff.Role.valueOf(rs.getString("role"));
                Staff staff = new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        role,
                        rs.getString("email"),
                        rs.getString("password")
                );
                staffList.add(staff);
            }
        }
        return staffList;
    }
}
