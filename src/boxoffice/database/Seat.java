package boxoffice.database;

public class Seat {
    private String rowNumber;
    private int seatNumber;
    private String restrictedView; // "Clear", "Partially Blocked", "Blocked"
    private boolean isAccessible;
    private boolean isBooked;
    private Location location;

    // Constructor
    public Seat(String rowNumber, int seatNumber, String restrictedView,
                boolean isAccessible, boolean isBooked, int roomID) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.restrictedView = restrictedView;
        this.isAccessible = isAccessible;
        this.isBooked = isBooked;
        roomID = location.getRoomId();
    }

    // Getters and Setters
    public String getRowNumber() { return rowNumber; }
    public void setRowNumber(String rowNumber) { this.rowNumber = rowNumber; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getRestrictedView() { return restrictedView; }
    public void setRestrictedView(String restrictedView) { this.restrictedView = restrictedView; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    @Override
    public String toString() {
        return "entity.Seat{" +
                "row_number='" + rowNumber + '\'' +
                ", seat_number=" + seatNumber +
                ", restricted_view='" + restrictedView + '\'' +
                ", is_accessible=" + isAccessible +
                ", is_booked=" + isBooked +
                ", room_id=" + location +
                '}';
    }
}
