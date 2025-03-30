package boxoffice.database;

public class Customer {
    private String customerID;
    private String customerName;
    private String phoneNumber;
    private String email;

    public Customer(){
    }
    Customer(String customerID, String customerName, String phoneNumber, String email){
        this.customerID = customerID;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
