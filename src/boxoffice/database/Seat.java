package boxoffice.database;

/**
 * Represents a single seat in a venue.
 * This class holds information such as the seat's unique ID, row, seat number, accessibility, and booking status.
 */
public class Seat {
    private String seatID;
    private String rowNumber;
    private int seatNumber;
    private boolean isAccesible;
    private boolean isBooked;

    /**
     * Constructs a Seat object with the specified details.
     *
     * @param seatID      The unique identifier for the seat.
     * @param rowNumber   The row letter/number where the seat is located.
     * @param seatNumber  The seat number within the row.
     * @param isAccessible A flag indicating whether the seat is accessible for people with disabilities.
     */
    public Seat(String seatID, String rowNumber, int seatNumber, boolean isAccessible) {
        this.seatID = seatID;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAccesible = isAccessible;
    }

    /**
     * Gets the row number or letter of the seat.
     *
     * @return The row number or letter.
     */
    public String getRowNumber() {
        return rowNumber;
    }

    /**
     * Sets the row number or letter for the seat.
     *
     * @param rowNumber The row number or letter to set.
     */
    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    /**
     * Gets the seat number within the row.
     *
     * @return The seat number within the row.
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number within the row.
     *
     * @param seatNumber The seat number to set.
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Gets the unique identifier for the seat.
     *
     * @return The seat's unique identifier.
     */
    public String getSeatID() {
        return seatID;
    }

    /**
     * Sets the unique identifier for the seat.
     *
     * @param seatID The unique seat ID to set.
     */
    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    /**
     * Gets the accessibility status of the seat.
     *
     * @return true if the seat is accessible, false otherwise.
     */
    public boolean isAccesible() {
        return isAccesible;
    }

    /**
     * Sets the accessibility status for the seat.
     *
     * @param accesible A flag indicating whether the seat is accessible.
     */
    public void setAccesible(boolean accesible) {
        isAccesible = accesible;
    }

    /**
     * Gets the booking status of the seat.
     *
     * @return true if the seat is booked, false otherwise.
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     * Sets the booking status for the seat.
     *
     * @param booked A flag indicating whether the seat is booked.
     */
    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    /**
     * Returns a string representation of the Seat object.
     * This includes the seat's ID, row, seat number, and accessibility status.
     *
     * @return A string representation of the seat.
     */
    @Override
    public String toString() {
        return "Seat{" +
                "seatID='" + seatID + '\'' +
                ", rowNumber='" + rowNumber + '\'' +
                ", seatNumber=" + seatNumber +
                ", isAccesible=" + isAccesible +
                ", isBooked=" + isBooked +
                '}';
    }
}
