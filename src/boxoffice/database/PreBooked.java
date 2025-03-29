package boxoffice.database;

public class PreBooked {
    private int preBookedId;
    private Performance performance;
    private Seat seat;
    private FOL fol;

    // Constructor
    public PreBooked(int preBookedId, Performance performance, Seat seat, FOL fol) {
        this.preBookedId = preBookedId;
        this.performance = performance;
        this.seat = seat;
        this.fol = fol;
    }

    // Getters and Setters
    public int getPreBookedId() { return preBookedId; }
    public void setPreBookedId(int preBookedId) { this.preBookedId = preBookedId; }

    public Performance getPerformance() { return performance; }
    public void setPerformance(Performance performance) { this.performance = performance; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public FOL getFol() { return fol; }
    public void setFol(FOL fol) { this.fol = fol; }
}