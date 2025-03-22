package boxoffice.models;

import java.util.List;

public class GroupBooking {
    private String groupName;
    private List<String> bookedSeats;
    private int numberOfSeats;

    public GroupBooking(String groupName, List<String> bookedSeats, int numberOfSeats) {
        this.groupName = groupName;
        this.bookedSeats = bookedSeats;
        this.numberOfSeats = numberOfSeats;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
