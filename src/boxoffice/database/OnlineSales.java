package boxoffice.database;

/**
 * Represents an online sale in the ticketing system.
 */
public class OnlineSales {
    private int onlineSaleId;
    private String buyerEmail;
    private String transactionId;
    private int ticketSaleID;

    /**
     * Constructs a new OnlineSales instance.
     *
     * @param onlineSaleId The unique ID for the online sale.
     * @param buyerEmail   The email address of the buyer.
     * @param transactionId The transaction ID for the sale.
     * @param ticketSaleID The related ticket sale ID.
     */
    public OnlineSales(int onlineSaleId, String buyerEmail,
                       String transactionId, int ticketSaleID) {
        this.onlineSaleId = onlineSaleId;
        this.buyerEmail = buyerEmail;
        this.transactionId = transactionId;
        this.ticketSaleID = ticketSaleID;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the online sale.
     */
    public int getOnlineSaleId() { return onlineSaleId; }

    /**
     * @param onlineSaleId The online sale ID to set.
     */
    public void setOnlineSaleId(int onlineSaleId) { this.onlineSaleId = onlineSaleId; }

    /**
     * @return The buyer's email address.
     */
    public String getBuyerEmail() { return buyerEmail; }

    /**
     * @param buyerEmail The buyer's email address to set.
     */
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    /**
     * @return The transaction ID for the online sale.
     */
    public String getTransactionId() { return transactionId; }

    /**
     * @param transactionId The transaction ID to set.
     */
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    /**
     * @return The ticket sale ID associated with this online sale.
     */
    public int getTicketSaleID() { return ticketSaleID; }

    /**
     * @param ticketSaleID The ticket sale ID to set.
     */
    public void setTicketSaleID(int ticketSaleID) { this.ticketSaleID = ticketSaleID; }
}
