package boxoffice.models;

import boxoffice.database.Staff;

public class Session {
    private static Session instance;
    private Staff currentStaff;
    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
    }

    public Staff getCurrentStaff() {
        return currentStaff;
    }

    public void clear() {
        this.currentStaff = null;
    }

    public boolean isLoggedIn() {
        return currentStaff != null;
    }
}
