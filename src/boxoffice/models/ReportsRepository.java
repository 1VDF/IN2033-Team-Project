package boxoffice.models;

import boxoffice.database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportsRepository {

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
                    double revenue = rs.getDouble("revenue");
                    revenueData.put(performanceTitle, revenue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueData;
    }


    public static Map<String, Integer> getDailyTicketSales(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> dailySales = new LinkedHashMap<>();
        String query = "SELECT DATE(sale_date) AS sale_date, COUNT(ticket_sale_id) AS sales " +
                "FROM ticket_sale " +
                "WHERE DATE(sale_date) BETWEEN ? AND ? " +
                "AND refunded = 0 " +
                "GROUP BY DATE(sale_date) " +
                "ORDER BY DATE(sale_date)";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String date = rs.getDate("sale_date").toLocalDate().toString();
                    int sales = rs.getInt("sales");
                    dailySales.put(date, sales);
                }
            }

            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                String dateStr = current.toString();
                if (!dailySales.containsKey(dateStr)) {
                    dailySales.put(dateStr, 0);
                }
                current = current.plusDays(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailySales;
    }

    public static Map<String, Double> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> dailyRevenue = new LinkedHashMap<>();
        String query = "SELECT DATE(sale_date) AS sale_date, SUM(price) AS revenue " +
                "FROM ticket_sale " +
                "WHERE DATE(sale_date) BETWEEN ? AND ? " +
                "AND refunded = 0 " +
                "GROUP BY DATE(sale_date) " +
                "ORDER BY DATE(sale_date)";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String date = rs.getDate("sale_date").toLocalDate().toString();
                    double revenue = rs.getDouble("revenue");
                    dailyRevenue.put(date, revenue);
                }
            }

            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                String dateStr = current.toString();
                if (!dailyRevenue.containsKey(dateStr)) {
                    dailyRevenue.put(dateStr, 0.0);
                }
                current = current.plusDays(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyRevenue;
    }



}


