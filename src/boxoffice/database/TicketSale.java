package boxoffice.database;


public class TicketSale {
    private int ticketSaleId;
    private double price;
    private Performance performance;
    private Discount discount;
    private GroupBooking groupBooking;
    private Staff staff;

    // Constructors
    public TicketSale() {}

    public TicketSale(int ticketSaleId, double price, Performance performance,
                      Discount discount, GroupBooking groupBooking, Staff staff) {
        this.ticketSaleId = ticketSaleId;
        this.price = price;
        this.performance = performance;
        this.discount = discount;
        this.groupBooking = groupBooking;
        this.staff = staff;
    }

    // Getters and Setters
    public int getTicketSaleId() { return ticketSaleId; }
    public void setTicketSaleId(int ticketSaleId) { this.ticketSaleId = ticketSaleId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Performance getPerformance() { return performance; }
    public void setPerformance(Performance performance) { this.performance = performance; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    public GroupBooking getGroupBooking() { return groupBooking; }
    public void setGroupBooking(GroupBooking groupBooking) { this.groupBooking = groupBooking; }

    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
}
