package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.FOL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FOLRepository {
    public List<FOL> getAllFriendsOfLancaster() throws SQLException {
        List<FOL> folList = new ArrayList<>();
        String sql = "SELECT * FROM fol";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                folList.add(new FOL(
                        rs.getInt("fol_id"),
                        rs.getString("customer_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return folList;
    }
}
