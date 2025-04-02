package boxoffice.database;

import java.util.List;

public class Venue {
    private int venueId;
    private String name;
    private int capacity;

    // Constructor
    public Venue(int venueId, String name, int capacity) {
        this.venueId = venueId;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }


    @Override
    public String toString() {
        return "entity.venue{" +
                "venue_id=" + venueId +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}