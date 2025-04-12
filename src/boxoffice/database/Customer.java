package boxoffice.database;

/**
 * Represents a customer in the box office system.
 * This class contains details about the customer, including their ID, name, phone number, and email.
 */
public class Customer {
    private String customerID;
    private String customerName;
    private String phoneNumber;
    private String email;

    /**
     * Default constructor for the Customer class.
     */
    public Customer(){
    }

    /**
     * Constructs a new Customer instance with the specified details.
     *
     * @param customerID     The unique ID of the customer.
     * @param customerName   The name of the customer.
     * @param phoneNumber    The phone number of the customer.
     * @param email          The email address of the customer.
     */
    public Customer(String customerID, String customerName, String phoneNumber, String email){
        this.customerID = customerID;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the customer.
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
     * @return The name of the customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customer name to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return The phone number of the customer.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phone number to set for the customer.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The email address of the customer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email address to set for the customer.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a string representation of the Customer object.
     *
     * @return A string representing the Customer object.
     */
    @Override
    public String toString() {
        return "entity.customer{" +
                "customer_id='" + customerID + '\'' +
                ", customer_name='" + customerName + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
