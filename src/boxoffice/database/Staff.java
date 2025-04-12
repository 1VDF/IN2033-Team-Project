package boxoffice.database;

/**
 * Represents a staff member working at the box office.
 * This class contains details about the staff member's personal information,
 * role, and login credentials.
 */
public class Staff {
    private int staffId;
    private String firstName;
    private String lastName;
    private Role role; // "Staff", "Manager", "Deputy Manager"
    private String email;
    private String password;

    /**
     * Enum representing the different roles a staff member can have.
     */
    public enum Role {
        Staff, Manager, DeputyManager
    }

    /**
     * Constructs a Staff object with the specified details.
     *
     * @param staffId    The unique identifier for the staff member.
     * @param firstName  The first name of the staff member.
     * @param lastName   The last name of the staff member.
     * @param role       The role of the staff member (Staff, Manager, or Deputy Manager).
     * @param email      The email address of the staff member.
     * @param password   The password for the staff member's account.
     */
    public Staff(int staffId, String firstName, String lastName, Role role, String email, String password) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the staff member.
     *
     * @return The staff member's ID.
     */
    public int getStaffId() {
        return staffId;
    }

    /**
     * Sets the unique identifier for the staff member.
     *
     * @param staffId The staff member's ID.
     */
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    /**
     * Gets the first name of the staff member.
     *
     * @return The staff member's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the staff member.
     *
     * @param firstName The staff member's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the staff member.
     *
     * @return The staff member's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the staff member.
     *
     * @param lastName The staff member's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the role of the staff member.
     *
     * @return The staff member's role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the staff member.
     *
     * @param role The staff member's role (Staff, Manager, or Deputy Manager).
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the email address of the staff member.
     *
     * @return The staff member's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the staff member.
     *
     * @param email The staff member's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password for the staff member's account.
     *
     * @return The staff member's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the staff member's account.
     *
     * @param password The staff member's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the Staff object.
     * This includes the staff's ID, name, role, email, and password.
     *
     * @return A string representation of the staff member.
     */
    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
