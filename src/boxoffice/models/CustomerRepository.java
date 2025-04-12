package boxoffice.models;

import boxoffice.database.Customer;
import boxoffice.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations related to the Customer entity.
 * This class allows retrieving, adding, and managing customer data in the database.
 */
public class CustomerRepository {

    /**
     * Retrieves a Customer from the database by their unique customer ID.
     *
     * @param customerId The unique ID of the customer.
     * @return A Customer object corresponding to the customer ID, or null if no customer is found.
     * @throws SQLException If an SQL error occurs.
     */
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

    /**
     * Retrieves a Customer from the database by their name.
     *
     * @param customerName The name of the customer.
     * @return A Customer object corresponding to the customer name, or null if no customer is found.
     * @throws SQLException If an SQL error occurs.
     */
    public static Customer getCustomerByName(String customerName) throws SQLException {
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

    /**
     * Adds a new customer to the database.
     *
     * @param customer The Customer object to be added.
     * @return true if the customer was successfully added, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
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

    /**
     * Retrieves a list of all customers from the database.
     *
     * @return A List of all Customer objects.
     * @throws SQLException If an SQL error occurs.
     */
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
