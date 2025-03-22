package boxoffice.models;

import java.time.LocalDate;
import java.util.List;

public class Show {
    private String showName;
    private LocalDate showDate;
    private List<String> availableSeats;

    public Show(String showName, LocalDate showDate, List<String> availableSeats) {
        this.showName = showName;
        this.showDate = showDate;
        this.availableSeats = availableSeats;
    }

    public String getShowName() {
        return showName;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public List<String> getAvailableSeats() {
        return availableSeats;
    }

    public void reserveSeat(String seat) {
        availableSeats.remove(seat);
    }

    public void cancelReservation(String seat) {
        availableSeats.add(seat);
    }
}
