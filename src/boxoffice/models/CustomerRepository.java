package boxoffice.models;

import boxoffice.database.Customer;
import boxoffice.database.DBConnection;

import java.sql.*;

public class CustomerRepository {
    public static Customer getCustomerById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                );
            }
            return null;
        }
    }

    public static Customer getCustomerByName(String customerName) throws SQLException{
        String sql = "SELECT * FROM customer WHERE customer_name = ?";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                );
            }
            return null;
        }
    }

    public static boolean addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (customer_id, customer_name, phone_number, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerID());
            stmt.setString(2, customer.getCustomerName());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getEmail());
            return stmt.executeUpdate() > 0;
        }
    }
}
