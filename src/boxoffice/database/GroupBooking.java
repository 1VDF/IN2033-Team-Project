package boxoffice.database;

public class GroupBooking {
    private int groupId;
    private String groupName;
    private String groupContact;
    private int numberOfTickets;
    private String customerID;

    // Constructor
    public GroupBooking(int groupId, String groupName, String groupContact, int numberOfTickets, String customerID) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupContact = groupContact;
        this.numberOfTickets = numberOfTickets;
        this.customerID = customerID;
    }

    // Getters and Setters
    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGroupContact() { return groupContact; }
    public void setGroupContact(String groupContact) { this.groupContact = groupContact; }

    public int getNumberOfTickets() { return numberOfTickets; }
    public void setNumberOfTickets(int numberOfTickets) { this.numberOfTickets = numberOfTickets; }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return "entity.group_booking{" +
                "group_id=" + groupId +
                ", group_name='" + groupName + '\'' +
                ", group_contact='" + groupContact + '\'' +
                ", number_of_tickets=" + numberOfTickets +
                ", customer_id=" + customerID +
                '}';
    }
}