package boxoffice.database;

/**
 * Represents a restricted view in a venue for a performance.
 * This class stores details about the restricted view, including its type, performance, and seat.
 */
public class RestrictedView {
    private int restrictedID;
    private int performanceID;
    private int seatID;

    /**
     * Enum representing the type of restricted view (partial or full).
     */
    public enum Type {
        partial, full
    }

    private Type type;

    /**
     * Constructs a RestrictedView object with the specified details.
     *
     * @param restrictedID The unique identifier for the restricted view.
     * @param performanceID The performance ID associated with the restricted view.
     * @param seatID The seat ID associated with the restricted view.
     * @param type The type of the restricted view (partial or full).
     */
    public RestrictedView(int restrictedID, int performanceID, int seatID, Type type) {
        this.restrictedID = restrictedID;
        this.performanceID = performanceID;
        this.seatID = seatID;
        this.type = type;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the restricted view.
     *
     * @return The restricted view ID.
     */
    public int getRestrictedID() {
        return restrictedID;
    }

    /**
     * Sets the unique identifier for the restricted view.
     *
     * @param restrictedID The restricted view ID.
     */
    public void setRestrictedID(int restrictedID) {
        this.restrictedID = restrictedID;
    }

    /**
     * Gets the performance ID associated with the restricted view.
     *
     * @return The performance ID.
     */
    public int getPerformanceID() {
        return performanceID;
    }

    /**
     * Sets the performance ID associated with the restricted view.
     *
     * @param performanceID The performance ID.
     */
    public void setPerformanceID(int performanceID) {
        this.performanceID = performanceID;
    }

    /**
     * Gets the seat ID associated with the restricted view.
     *
     * @return The seat ID.
     */
    public int getSeatID() {
        return seatID;
    }

    /**
     * Sets the seat ID associated with the restricted view.
     *
     * @param seatID The seat ID.
     */
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    /**
     * Gets the type of the restricted view (partial or full).
     *
     * @return The type of the restricted view.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the restricted view (partial or full).
     *
     * @param type The type of the restricted view.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns a string representation of the RestrictedView object.
     * This includes the restricted view's ID, performance ID, seat ID, and view type.
     *
     * @return A string representation of the restricted view.
     */
    @Override
    public String toString() {
        return "RestrictedView{" +
                "restrictedID=" + restrictedID +
                ", performanceID=" + performanceID +
                ", seatID=" + seatID +
                ", type=" + type +
                '}';
    }
}
