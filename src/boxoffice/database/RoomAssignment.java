package boxoffice.database;

public class RoomAssignment {
    private int roomAssignmentId;
    private Performance performance;
    private Location location;

    // Constructor
    public RoomAssignment(int roomAssignmentId, Performance performance, Location location) {
        this.roomAssignmentId = roomAssignmentId;
        this.performance = performance;
        this.location = location;
    }

    // Getters and Setters
    public int getRoomAssignmentId() { return roomAssignmentId; }
    public void setRoomAssignmentId(int roomAssignmentId) { this.roomAssignmentId = roomAssignmentId; }

    public Performance getPerformance() { return performance; }
    public void setPerformance(Performance performance) { this.performance = performance; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
