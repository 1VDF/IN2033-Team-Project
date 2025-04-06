package boxoffice.database;

public class Staff {
    private int staffId;
    private String firstName;
    private String lastName;
    private Role role; // "Staff", "Manager", "Deputy Manager"
    private String email;
    private String password;

    public enum Role {
        Staff, Manager, DeputyManager
    }

    // Constructor
    public Staff(int staffId, String firstName, String lastName,
                 Role role, String email, String password) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.password = password;
    }


    // Getters and Setters
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "entity.staff{" +
                "staff_id=" + staffId +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}