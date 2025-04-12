
package boxoffice.database;

import java.time.LocalDate;

/**
 * Represents a refund in the ticketing system.
 */
public class Refund {
    private int refundId;
    private LocalDate date;
    private double refundAmount;
    private String refundReason;
    private String refundStatus;
    private int ticketSaleID;
    private int staffID;

    /**
     * Constructs a new Refund instance.
     *
     * @param refundId     The unique ID for the refund.
     * @param date         The date of the refund.
     * @param refundAmount The amount of money refunded.
     * @param refundReason The reason for the refund.
     * @param refundStatus The status of the refund (e.g., "Done", "Processing").
     * @param ticketSaleID The ID of the related ticket sale.
     * @param staffID      The ID of the staff handling the refund.
     */
    public Refund(int refundId, LocalDate date, Double refundAmount,
                  String refundReason, String refundStatus, int ticketSaleID, int staffID) {
        this.refundId = refundId;
        this.date = date;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.ticketSaleID = ticketSaleID;
        this.staffID = staffID;
    }

    // Getters and Setters methods

    /**
     * @return The unique ID of the refund.
     */
    public int getRefundId() { return refundId; }

    /**
     * @param refundId The unique ID to set for the refund.
     */
    public void setRefundId(int refundId) { this.refundId = refundId; }

    /**
     * @return The date of the refund.
     */
    public LocalDate getDate() { return date; }

    /**
     * @param date The date to set for the refund.
     */
    public void setDate(LocalDate date) { this.date = date; }

    /**
     * @return The amount of money refunded.
     */
    public double getRefundAmount() { return refundAmount; }

    /**
     * @param refundAmount The amount to set for the refund.
     */
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }

    /**
     * @return The reason for the refund.
     */
    public String getRefundReason() { return refundReason; }

    /**
     * @param refundReason The reason to set for the refund.
     */
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    /**
     * @return The status of the refund.
     */
    public String getRefundStatus() { return refundStatus; }

    /**
     * @param refundStatus The status to set for the refund.
     */
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }

    /**
     * @return The ID of the related ticket sale.
     */
    public int getTicketSaleID() { return ticketSaleID; }

    /**
     * @param ticketSaleID The ticket sale ID to set.
     */
    public void setTicketSaleID(int ticketSaleID) { this.ticketSaleID = ticketSaleID; }

    /**
     * @return The ID of the staff handling the refund.
     */
    public int getStaffID() { return staffID; }

    /**
     * @param staffID The staff ID to set.
     */
    public void setStaffID(int staffID) { this.staffID = staffID; }

    /**
     * Returns a string representation of the Refund object.
     *
     * @return A string representing the Refund object.
     */
    @Override
    public String toString() {
        return "Refund{" +
                "refundId=" + refundId +
                ", date=" + date +
                ", refundAmount=" + refundAmount +
                ", refundReason='" + refundReason + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", ticketSaleID=" + ticketSaleID +
                ", staffID=" + staffID +
                '}';
    }
}
