package boxoffice.database;

/**
 * Represents a FOL (Friend of the Library) membership in the box office system.
 * This class contains details about the FOL membership, including the FOL ID and the associated customer ID.
 */
public class FOL {
    private int folId;
    private String customerID;

    /**
     * Constructs a new FOL instance.
     *
     * @param folId      The unique ID of the FOL membership.
     * @param customerID The customer ID associated with the FOL membership.
     */
    public FOL(int folId, String customerID ) {
        this.folId = folId;
        this.customerID = customerID;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the FOL membership.
     */
    public int getFolId() { return folId; }

    /**
     * @param folId The FOL ID to set.
     */
    public void setFolId(int folId) { this.folId = folId; }

    /**
     * @return The customer ID associated with the FOL membership.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID The customer ID to set.
     */
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    /**
     * Returns a string representation of the FOL object.
     *
     * @return A string representing the FOL object.
     */
    @Override
    public String toString() {
        return "entity.fol{" +
                "fol_id=" + folId +
                ", customer_id=" + customerID +
                '}';
    }
}
