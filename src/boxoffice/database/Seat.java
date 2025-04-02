package boxoffice.database;

public class Seat {
    private String seatID;
    private String rowNumber;
    private int seatNumber;
    private boolean isAccesible;

    private boolean isBooked;

    // Constructor
    public Seat(String seatID, String rowNumber, int seatNumber,boolean isAccessible) {
        this.seatID = seatID;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAccesible = isAccessible;
    }

    // Getters and Setters
    public String getRowNumber() { return rowNumber; }
    public void setRowNumber(String rowNumber) { this.rowNumber = rowNumber; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public boolean isAccesible() {
        return isAccesible;
    }

    public void setAccesible(boolean accesible) {
        isAccesible = accesible;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "entity.seat{" +
                "seat_id=" + seatID +
                ", row_number='" + rowNumber + '\'' +
                ", seat_number=" + seatNumber +
                ", is_accesible='" + isAccesible + '\'' +
                '}';
    }
}
