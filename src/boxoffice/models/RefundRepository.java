package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Refund;

import java.sql.*;

/**
 * Repository class to handle refund operations, interacting with the 'refund' table in the database.
 */
public class RefundRepository {

    /**
     * Adds a new refund record to the database.
     *
     * @param refund The Refund object containing details of the refund.
     * @return true if the refund was successfully added, otherwise false.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean addRefund(Refund refund) throws SQLException {
        String query = "INSERT INTO refund (date, refund_amount, refund_reason, refund_status, ticket_sale_id, staff_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(refund.getDate()));
            stmt.setDouble(2, refund.getRefundAmount());
            stmt.setString(3, refund.getRefundReason());
            stmt.setString(4, refund.getRefundStatus());
            stmt.setInt(5, refund.getTicketSaleID());
            stmt.setInt(6, refund.getStaffID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
