package boxoffice.database;

public class OnlineSales {
    private int onlineSaleId;
    private String buyerEmail;
    private String transactionId;
    private TicketSale ticketSale; // Associated ticket sale

    // Constructors
    public OnlineSales(int onlineSaleId, String buyerEmail,
                      String transactionId, TicketSale ticketSale) {
        this.onlineSaleId = onlineSaleId;
        this.buyerEmail = buyerEmail;
        this.transactionId = transactionId;
        this.ticketSale = ticketSale;
    }

    // Getters and Setters
    public int getOnlineSaleId() { return onlineSaleId; }
    public void setOnlineSaleId(int onlineSaleId) { this.onlineSaleId = onlineSaleId; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public TicketSale getTicketSale() { return ticketSale; }
    public void setTicketSale(TicketSale ticketSale) { this.ticketSale = ticketSale; }
}
