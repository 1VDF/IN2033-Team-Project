package boxoffice.models;

public class Ticket {
    private String ticketCode;
    private String showName;
    private String seat;
    private String customerName;
    private boolean isRefunded;

    public Ticket(String ticketCode, String showName, String seat, String customerName) {
        this.ticketCode = ticketCode;
        this.showName = showName;
        this.seat = seat;
        this.customerName = customerName;
        this.isRefunded = false;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public String getShowName() {
        return showName;
    }

    public String getSeat() {
        return seat;
    }

    public String getCustomerName() {
        return customerName;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setRefunded(boolean refunded) {
        this.isRefunded = refunded;
    }
}
