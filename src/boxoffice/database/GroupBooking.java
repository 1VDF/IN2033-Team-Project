package boxoffice.database;

/**
 * Represents a group booking in the box office system.
 * This class contains details about the group, including the group ID, group name, contact information,
 * number of tickets, and the associated customer ID.
 */
public class GroupBooking {
    private int groupId;
    private String groupName;
    private String groupContact;
    private int numberOfTickets;
    private String customerID;

    /**
     * Constructs a new GroupBooking instance.
     *
     * @param groupId       The unique ID of the group booking.
     * @param groupName     The name of the group.
     * @param groupContact  The contact information of the group.
     * @param numberOfTickets The number of tickets in the booking.
     * @param customerID    The customer ID who made the booking.
     */
    public GroupBooking(int groupId, String groupName, String groupContact, int numberOfTickets, String customerID) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupContact = groupContact;
        this.numberOfTickets = numberOfTickets;
        this.customerID = customerID;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the group booking.
     */
    public int getGroupId() { return groupId; }

    /**
     * @param groupId The group ID to set.
     */
    public void setGroupId(int groupId) { this.groupId = groupId; }

    /**
     * @return The name of the group.
     */
    public String getGroupName() { return groupName; }

    /**
     * @param groupName The group name to set.
     */
    public void setGroupName(String groupName) { this.groupName = groupName; }

    /**
     * @return The contact information of the group.
     */
    public String getGroupContact() { return groupContact; }

    /**
     * @param groupContact The group contact information to set.
     */
    public void setGroupContact(String groupContact) { this.groupContact = groupContact; }

    /**
     * @return The number of tickets in the booking.
     */
    public int getNumberOfTickets() { return numberOfTickets; }

    /**
     * @param numberOfTickets The number of tickets to set.
     */
    public void setNumberOfTickets(int numberOfTickets) { this.numberOfTickets = numberOfTickets; }

    /**
     * @return The customer ID who made the booking.
     */
    public String getCustomerID() { return customerID; }

    /**
     * @param customerID The customer ID to set.
     */
    public void setCustomerID(String customerID) { this.customerID = customerID; }

    /**
     * Returns a string representation of the GroupBooking object.
     *
     * @return A string representing the GroupBooking object.
     */
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
