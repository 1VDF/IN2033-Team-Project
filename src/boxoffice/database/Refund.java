package boxoffice.database;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Refund {
    private int refundId;
    private LocalDate date;
    private double refundAmount;
    private String refundReason;
    private String refundStatus; // "Done", "Processing"
    private int ticketSaleID;
    private int staffID;

    // Constructor
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

    // Getters and Setters
    public int getRefundId() { return refundId; }
    public void setRefundId(int refundId) { this.refundId = refundId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }

    public int getTicketSaleID() {
        return ticketSaleID;
    }

    public void setTicketSaleID(int ticketSaleID) {
        this.ticketSaleID = ticketSaleID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    @Override
    public String toString() {
        return "entity.refund{" +
                "refund_id=" + refundId +
                ", date=" + date +
                ", refund_amount=" + refundAmount +
                ", refund_reason='" + refundReason + '\'' +
                ", refund_status='" + refundStatus + '\'' +
                ", ticket_sale_id=" + ticketSaleID +
                ", staff_id=" + staffID +
                '}';
    }
}
