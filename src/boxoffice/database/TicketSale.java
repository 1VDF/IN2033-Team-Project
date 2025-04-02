package boxoffice.database;


import static java.sql.Types.NULL;

public class TicketSale {
    private int ticketSaleId;
    private double price;
    private int performanceID;
    private String seatID;
    private int discountID;
    private int groupBookingID;
    private int staffID;
    private String customerID;

    // Constructors
    public TicketSale() {}

    public TicketSale(int ticketSaleId, double price, String customerID, int performanceID, String seatID,
                      int discountID, int groupBookingID, int staffID) {
        this.ticketSaleId = ticketSaleId;
        this.price = price;
        this.performanceID = performanceID;
        this.seatID = seatID;
        this.discountID = discountID;
        this.groupBookingID = groupBookingID;
        this.staffID = staffID;
        this.customerID = customerID;
    }

    // Getters and Setters
    public int getTicketSaleId() { return ticketSaleId; }
    public void setTicketSaleId(int ticketSaleId) { this.ticketSaleId = ticketSaleId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getPerformanceID() {
        return performanceID;
    }

    public void setPerformanceID(int performanceID) {
        this.performanceID = performanceID;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public int getGroupBookingID() {
        return groupBookingID;
    }

    public void setGroupBookingID(int groupBookingID) {
        this.groupBookingID = groupBookingID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return "entity.ticket_sale{" +
                "ticket_sale_id=" + ticketSaleId +
                ", price=" + price +
                ", customer_id=" + customerID +
                ", performance_id=" + performanceID +
                ", seat_id=" + seatID +
                ", discount_id=" + discountID +
                ", group_id=" + groupBookingID +
                ", staff_id=" + staffID +
                '}';
    }
}
