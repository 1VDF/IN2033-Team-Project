package boxoffice.database;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Refund {
    private int refundId;
    private LocalDate date;
    private double refundAmount;
    private String refundReason;
    private String refundStatus; // "Done", "Processing"
    private TicketSale ticketSale;
    private Staff staff;

    // Constructor
    public Refund(int refundId, LocalDate date, Double refundAmount,
                  String refundReason, String refundStatus, TicketSale ticketSale, Staff staff) {
        this.refundId = refundId;
        this.date = date;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.ticketSale = ticketSale;
        this.staff = staff;
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

    public TicketSale getTicketSale() { return ticketSale; }
    public void setTicketSale(TicketSale ticketSale) { this.ticketSale = ticketSale; }

    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
}
