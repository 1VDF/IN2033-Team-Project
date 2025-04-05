package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffRepository {
    public Staff getStaffByCredentials(String email, String password) throws SQLException {
        String query = "SELECT * FROM staff WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getInt("staff_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("role"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff ORDER BY role, last_name, first_name";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        }
        return staffList;
    }


    public void addStaff(Staff staff) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
            conn.setAutoCommit(false);

            int newId = getNextStaffId(conn);

            String query = "INSERT INTO staff (staff_id, first_name, last_name, role, email, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, newId);
                stmt.setString(2, staff.getFirstName());
                stmt.setString(3, staff.getLastName());
                stmt.setString(4, staff.getRole());
                stmt.setString(5, staff.getEmail());
                stmt.setString(6, staff.getPassword());

                stmt.executeUpdate();
                staff.setStaffId(newId);
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }


    private int getNextStaffId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(staff_id) FROM staff")) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
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
}