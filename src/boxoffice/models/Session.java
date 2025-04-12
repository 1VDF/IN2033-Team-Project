package boxoffice.models;

import boxoffice.database.Staff;

/**
 * Singleton class that manages the current session, specifically for storing the logged-in staff information.
 */
public class Session {
    private static Session instance;
    private Staff currentStaff;

    private Session() {
    }

    /**
     * Gets the instance of the Session.
     *
     * @return The singleton instance of the Session.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Sets the current staff in the session.
     *
     * @param staff The staff to set as the current user.
     */
    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
    }

    /**
     * Gets the current staff in the session.
     *
     * @return The current logged-in staff.
     */
    public Staff getCurrentStaff() {
        return currentStaff;
    }

    /**
     * Clears the current staff from the session.
     */
    public void clear() {
        this.currentStaff = null;
    }

    /**
     * Checks if a staff member is logged in.
     *
     * @return True if a staff member is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentStaff != null;
    }
}
