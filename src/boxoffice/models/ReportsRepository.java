package boxoffice.models;

import boxoffice.database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportsRepository {

    // Method for getting monthly revenue
    public static Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> revenueData = new LinkedHashMap<>();
        String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(price) AS revenue FROM ticket_sale GROUP BY month ORDER BY month";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                revenueData.put(rs.getString("month"), rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueData;
    }

    // Method for getting ticket sales by performance within a date range (excluding refunded)
    public static Map<String, Integer> getTicketSalesByPerformanceForDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        Map<String, Integer> salesData = new LinkedHashMap<>();
        String query = "SELECT p.title, COUNT(ts.ticket_sale_id) AS sales " +
                "FROM ticket_sale ts " +
                "JOIN performance p ON ts.performance_id = p.performance_id " +
                "WHERE DATE(ts.sale_date) BETWEEN ? AND ? " +
                "AND ts.refunded = 0 " +
                "GROUP BY p.title";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String performanceTitle = rs.getString("title");
                    int sales = rs.getInt("sales");
                    salesData.put(performanceTitle, sales);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesData;
    }

    // Method for getting revenue by performance within a date range (excluding refunded)
    public static Map<String, Double> getRevenueByPerformanceForDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        Map<String, Double> revenueData = new LinkedHashMap<>();
        String query = "SELECT p.title, SUM(ts.price) AS revenue " +
                "FROM ticket_sale ts " +
                "JOIN performance p ON ts.performance_id = p.performance_id " +
                "WHERE DATE(ts.sale_date) BETWEEN ? AND ? " +  // Use DATE() to ignore time
                "AND ts.refunded = 0 " + // Exclude refunded tickets
                "GROUP BY p.title";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String performanceTitle = rs.getString("title");
                    double revenue = rs.getDouble("revenue");
                    revenueData.put(performanceTitle, revenue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueData;
    }

}
