package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Staff;
import java.sql.*;

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
}