package boxoffice.database;

/**
 * Represents a venue in the box office system.
 * This class contains details about the venue, such as its ID, name, and capacity.
 */
public class Venue {
    private int venueId;
    private String name;
    private int capacity;

    /**
     * Constructs a Venue object with the specified details.
     *
     * @param venueId   The unique identifier for the venue.
     * @param name      The name of the venue.
     * @param capacity  The seating capacity of the venue.
     */
    public Venue(int venueId, String name, int capacity) {
        this.venueId = venueId;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the venue.
     *
     * @return The venue's ID.
     */
    public int getVenueId() {
        return venueId;
    }

    /**
     * Sets the unique identifier for the venue.
     *
     * @param venueId The venue's ID.
     */
    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    /**
     * Gets the name of the venue.
     *
     * @return The venue's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the venue.
     *
     * @param name The venue's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the seating capacity of the venue.
     *
     * @return The venue's seating capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the seating capacity of the venue.
     *
     * @param capacity The venue's seating capacity.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns a string representation of the Venue object.
     * This includes the venue's ID, name, and seating capacity.
     *
     * @return A string representation of the venue.
     */
    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
