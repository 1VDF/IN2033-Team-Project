package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffRepository {

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

    public void deleteStaff(int staffId) throws SQLException {
        String query = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        }
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff ORDER BY role, last_name, first_name";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Get the role string from the database and normalize it
                String roleString = rs.getString("role");
                Staff.Role role;
                if (roleString == null) {
                    role = Staff.Role.Staff; // Default for null
                } else {
                    // Normalize: remove spaces, capitalize first letter of each word
                    String normalizedRole = roleString.trim().replace(" ", "");
                    // Convert to match enum (e.g., "Deputy Manager" -> "DeputyManager")
                    if (normalizedRole.equalsIgnoreCase("Staff")) {
                        role = Staff.Role.Staff;
                    } else if (normalizedRole.equalsIgnoreCase("DeputyManager")) {
                        role = Staff.Role.DeputyManager;
                    } else if (normalizedRole.equalsIgnoreCase("Manager")) {
                        role = Staff.Role.Manager;
                    } else {
                        System.err.println("Invalid role in database: " + roleString + ". Defaulting to Staff.");
                        role = Staff.Role.Staff; // Fallback
                    }
                }

                staffList.add(new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        role,
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw e;
        }
        return staffList;
    }
}
