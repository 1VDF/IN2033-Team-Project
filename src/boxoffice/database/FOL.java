package boxoffice.database;

public class FOL {
    private int folId;
    private String customerID;

    // Constructor
    public FOL(int folId, String customerID ) {
        this.folId = folId;
        this.customerID = customerID;
    }

    // Getters and Setters
    public int getFolId() { return folId; }
    public void setFolId(int folId) { this.folId = folId; }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return "entity.fol{" +
                "fol_id=" + folId +
                ", customer_id=" + customerID +
                '}';
    }
}
