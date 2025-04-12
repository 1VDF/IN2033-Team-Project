package boxoffice.models;

import boxoffice.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Types.NULL;

/**
 * Provides database operations related to the Discount entity.
 * This class allows retrieving discount information from the database based on ID or type.
 */
public class DiscountRepository {

    /**
     * Retrieves the discount value by its unique ID.
     *
     * @param discountId The unique ID of the discount.
     * @return The value of the discount.
     * @throws SQLException If an SQL error occurs.
     */
    public static double getDiscountValueById(int discountId) throws SQLException {
        String sql = "SELECT discount_value, is_percentage FROM discount WHERE discount_id = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, discountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double value = rs.getDouble("discount_value");
                boolean isPercentage = rs.getBoolean("is_percentage");
                return isPercentage ? value : value; // Adjust based on your needs
            }
            return 0.0;
        } catch (SQLException e) {
            System.err.println("Error getting discount value for ID: " + discountId);
            throw e;
        }
    }

    /**
     * Retrieves the discount ID by its type.
     *
     * @param discountName The type of discount (e.g., "NHS", "Military").
     * @return The unique ID of the discount, or NULL if no discount type is found.
     * @throws SQLException If an SQL error occurs.
     */
    public static Integer getDiscountIdByName(String discountName) throws SQLException {
        if (discountName == null || discountName.isEmpty()) {
            return NULL;
        }

        String sql = "SELECT discount_id FROM discount WHERE discount_type = ?";

        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, discountName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("discount_id");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error getting discount ID for code: " + discountName);
            throw e;
        }
    }
}
